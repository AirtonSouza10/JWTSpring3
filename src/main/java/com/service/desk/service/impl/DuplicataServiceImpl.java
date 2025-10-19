package com.service.desk.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.desk.dto.DuplicataRequestDTO;
import com.service.desk.dto.DuplicataResponseDTO;
import com.service.desk.dto.NotaFiscalResponseDTO;
import com.service.desk.dto.ParcelamentoDTO;
import com.service.desk.entidade.Duplicata;
import com.service.desk.entidade.NotaFiscal;
import com.service.desk.entidade.Parcela;
import com.service.desk.enumerator.MensagemEnum;
import com.service.desk.exceptions.NegocioException;
import com.service.desk.repository.DuplicataRepository;
import com.service.desk.repository.FormaPagamentoRepository;
import com.service.desk.repository.NotaFiscalRepository;
import com.service.desk.repository.ParcelaRepository;
import com.service.desk.service.service.DuplicataService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class DuplicataServiceImpl implements DuplicataService {
	

    @Autowired
    private DuplicataRepository duplicataRepository;

    @Autowired
    private ParcelaRepository parcelaRepository;
    
    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;
    
    @Autowired
    private NotaFiscalRepository notaFiscalRepository;

    @Override
    public List<DuplicataResponseDTO> listarDuplicatas() {
        var duplicatas = duplicataRepository.findAll();

        return duplicatas.stream().map(d -> DuplicataResponseDTO.builder()
                .id(d.getId())
                .descricao(d.getDescricao())
                .valor(d.getValor())
                .desconto(d.getDesconto())
                .multa(d.getMulta())
                .juros(d.getJuros())
                .valorTotal(d.getValorTotal())
                .formaPagamentoId(Objects.nonNull(d.getFormaPagamento()) ? d.getFormaPagamento().getId() : null)
                .dtCriacao(d.getDtCriacao() != null ? new java.sql.Date(d.getDtCriacao().getTime()) : null)
                .dtAtualizacao(d.getDtAtualizacao() != null ? new java.sql.Date(d.getDtAtualizacao().getTime()) : null)
                .parcelas(d.getParcelas() != null ? d.getParcelas().stream().map(p ->
                        ParcelamentoDTO.builder()
                                .id(p.getId())
                                .valorTotal(p.getValorTotal())
                                .dtVencimento(p.getDtVencimento() != null ? new java.sql.Date(p.getDtVencimento().getTime()) : null)
                                .dtCriacao(p.getDtCriacao() != null ? new java.sql.Date(p.getDtCriacao().getTime()) : null)
                                .dtAtualizacao(p.getDtAtualizacao() != null ? new java.sql.Date(p.getDtAtualizacao().getTime()) : null)
                                .duplicataId(d.getId())
                                .build()
                ).collect(Collectors.toList()) : null)
                .notasFiscais(d.getNotasFiscais() != null ? d.getNotasFiscais().stream().map(nf ->
		                NotaFiscalResponseDTO.builder()
		                        .id(nf.getId())
		                        .chave(nf.getChave())
		                        .numero(nf.getNumero())
		                        .serie(nf.getSerie())
		                        .dtCompra(nf.getDtCompra())
		                        .fornecedorId(nf.getFornecedor().getId())
		                        .fornecedorNome(nf.getFornecedor().getNome())
		                        .filialId(nf.getFilial().getId())
		                        .tipoNotaId(nf.getTipo().getId())
		                        .valorTotal(nf.getValorTotal())
		                        .build()
		        ).collect(Collectors.toList()) : null)
                .build()).toList();
    }

    @Override
    public DuplicataResponseDTO buscarDuplicataPorId(Long id) {
        var duplicata = duplicataRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Duplicata não encontrada para id " + id));

        List<ParcelamentoDTO> parcelasDTO = duplicata.getParcelas().stream().map(p ->
                ParcelamentoDTO.builder()
                        .id(p.getId())
                        .valorTotal(p.getValorTotal())
                        .dtVencimento(p.getDtVencimento() != null ? new java.sql.Date(p.getDtVencimento().getTime()) : null)
                        .dtCriacao(p.getDtCriacao() != null ? new java.sql.Date(p.getDtCriacao().getTime()) : null)
                        .dtAtualizacao(p.getDtAtualizacao() != null ? new java.sql.Date(p.getDtAtualizacao().getTime()) : null)
                        .duplicataId(duplicata.getId())
                        .build()
        ).collect(Collectors.toList());
        
        List<NotaFiscalResponseDTO> notaFiscalResponseDTO = duplicata.getNotasFiscais().stream().map(nf ->
		        NotaFiscalResponseDTO.builder()
		        .id(nf.getId())
		        .chave(nf.getChave())
		        .numero(nf.getNumero())
		        .serie(nf.getSerie())
		        .dtCompra(nf.getDtCompra())
		        .fornecedorId(nf.getFornecedor().getId())
		        .fornecedorNome(nf.getFornecedor().getNome())
		        .filialId(nf.getFilial().getId())
		        .tipoNotaId(nf.getTipo().getId())
		        .valorTotal(nf.getValorTotal())
		        .build()
		).collect(Collectors.toList());

        return DuplicataResponseDTO.builder()
                .id(duplicata.getId())
                .descricao(duplicata.getDescricao())
                .valor(duplicata.getValor())
                .desconto(duplicata.getDesconto())
                .multa(duplicata.getMulta())
                .valorTotal(duplicata.getValorTotal())
                .formaPagamentoId(duplicata.getFormaPagamento().getId())
                .dtCriacao(duplicata.getDtCriacao() != null ? new java.sql.Date(duplicata.getDtCriacao().getTime()) : null)
                .dtAtualizacao(duplicata.getDtAtualizacao() != null ? new java.sql.Date(duplicata.getDtAtualizacao().getTime()) : null)
                .parcelas(parcelasDTO)
                .notasFiscais(notaFiscalResponseDTO)
                .build();
    }

    @Override
    @Transactional
    public void salvarDuplicata(DuplicataRequestDTO dto) {
        Duplicata duplicata = new Duplicata();
        duplicata.setDescricao(dto.getDescricao());
        duplicata.setValor(dto.getValor());
        duplicata.setDesconto(dto.getDesconto());
        duplicata.setJuros(dto.getJuros());
        duplicata.setMulta(dto.getMulta());
        duplicata.setValorTotal(dto.getValorTotal());
        var formaPagamento = formaPagamentoRepository.findById(dto.getFormaPagamentoId()).orElseThrow();
        duplicata.setFormaPagamento(formaPagamento);
        duplicata.setDtCriacao(new java.util.Date());
        
        // Notas fiscais associadas
        if (dto.getNotasFiscais() != null && !dto.getNotasFiscais().isEmpty()) {
            List<NotaFiscal> notas = dto.getNotasFiscais().stream()
                .map(nfDto -> {
                    NotaFiscal nf = notaFiscalRepository.findById(nfDto.getId()).orElseThrow();
                    nf.setDuplicata(duplicata);
                    return nf;
                })
                .toList();

            duplicata.setNotasFiscais(notas);
        } else {
            duplicata.setNotasFiscais(Collections.emptyList());
        }

        duplicataRepository.save(duplicata);

        if (dto.getParcelas() != null) {
            for (var parcDto : dto.getParcelas()) {
                Parcela parcela = new Parcela();
                parcela.setDuplicata(duplicata);
                parcela.setValorTotal(parcDto.getValorTotal());
                parcela.setDtVencimento(parcDto.getDtVencimento());
                parcela.setDtCriacao(new java.util.Date());
                parcelaRepository.save(parcela);
            }
        }
    }

    @Override
    @Transactional
    public void atualizarDuplicata(Long id, DuplicataRequestDTO dto) {
        var duplicata = duplicataRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Duplicata não encontrada para id " + id));

        duplicata.setDescricao(dto.getDescricao());
        duplicata.setValor(dto.getValor());
        duplicata.setDesconto(dto.getDesconto());
        duplicata.setMulta(dto.getMulta());
        duplicata.setJuros(dto.getJuros());
        duplicata.setValorTotal(dto.getValorTotal());
        var formaPagamento = formaPagamentoRepository.findById(dto.getFormaPagamentoId()).orElseThrow(() -> new NegocioException(MensagemEnum.MSGE010.getKey()));
        duplicata.setFormaPagamento(formaPagamento);
        duplicata.setDtAtualizacao(new java.util.Date());
        

        // Atualizar notas fiscais associadas
        List<NotaFiscal> novasNotas = (dto.getNotasFiscais() != null && !dto.getNotasFiscais().isEmpty())
        	    ? dto.getNotasFiscais().stream()
        	        .map(nfDto -> notaFiscalRepository.findById(nfDto.getId())
        	            .orElseThrow(() -> new NegocioException(MensagemEnum.MSGE010.getKey())))
        	        .peek(nf -> nf.setDuplicata(duplicata))
        	        .toList()
        	    : List.of();

        // Desvincular as antigas
        duplicata.getNotasFiscais().forEach(nf -> nf.setDuplicata(null));
        duplicata.getNotasFiscais().clear();

        // Vincular as novas
        novasNotas.forEach(nf -> nf.setDuplicata(duplicata));
        duplicata.getNotasFiscais().addAll(novasNotas);
        

        var parcelasAtuais = parcelaRepository.findByDuplicataId(duplicata.getId());

        if (dto.getParcelas() != null) {
            for (var parcelaDto : dto.getParcelas()) {
                if (parcelaDto.getId() != null) {
                    var existente = parcelasAtuais.stream()
                            .filter(p -> p.getId().equals(parcelaDto.getId()))
                            .findFirst()
                            .orElse(null);

                    if (existente != null) {
                        existente.setDtVencimento(parcelaDto.getDtVencimento());
                        existente.setValorTotal(parcelaDto.getValorTotal());
                        parcelaRepository.save(existente);
                    }
                } else {
                    var nova = Parcela.builder()
                            .duplicata(duplicata)
                            .dtVencimento(parcelaDto.getDtVencimento())
                            .valorTotal(parcelaDto.getValorTotal())
                            .build();
                    parcelaRepository.save(nova);
                }
            }
        }

        var idsEnviados = dto.getParcelas() != null
                ? dto.getParcelas().stream()
                        .map(ParcelamentoDTO::getId)
                        .filter(Objects::nonNull)
                        .toList()
                : List.<Long>of();

        for (var parcela : parcelasAtuais) {
            if (!idsEnviados.contains(parcela.getId())) {
                parcelaRepository.delete(parcela);
            }
        }

        duplicataRepository.save(duplicata);
    }

    @Override
    @Transactional
    public void excluirDuplicata(Long id) {
        var duplicata = duplicataRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Duplicata não encontrada para id " + id));

        parcelaRepository.deleteAll(duplicata.getParcelas());
        duplicataRepository.delete(duplicata);
    }	
}
