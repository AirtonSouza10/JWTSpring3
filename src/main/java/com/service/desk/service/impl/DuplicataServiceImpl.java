package com.service.desk.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.desk.dto.DuplicataRequestDTO;
import com.service.desk.dto.DuplicataResponseDTO;
import com.service.desk.dto.ParcelamentoDTO;
import com.service.desk.entidade.Duplicata;
import com.service.desk.entidade.Parcela;
import com.service.desk.repository.DuplicataRepository;
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

    @Override
    public List<DuplicataResponseDTO> listarDuplicatas() {
        var duplicatas = duplicataRepository.findAll();

        return duplicatas.stream().map(d -> DuplicataResponseDTO.builder()
                .id(d.getId())
                .descricao(d.getDescricao())
                .valor(d.getValor())
                .desconto(d.getDesconto())
                .multa(d.getMulta())
                .valorTotal(d.getValorTotal())
                .dtCriacao(d.getDtCriacao() != null ? new java.sql.Date(d.getDtCriacao().getTime()) : null)
                .dtAtualizacao(d.getDtAtualizacao() != null ? new java.sql.Date(d.getDtAtualizacao().getTime()) : null)
                .parcelas(d.getParcelas() != null ? d.getParcelas().stream().map(p ->
                        ParcelamentoDTO.builder()
                                .id(p.getId())
                                .valor(p.getValor())
                                .desconto(p.getDesconto())
                                .multa(p.getMulta())
                                .valorTotal(p.getValorTotal())
                                .dtVencimento(p.getDtVencimento() != null ? new java.sql.Date(p.getDtVencimento().getTime()) : null)
                                .dtCriacao(p.getDtCriacao() != null ? new java.sql.Date(p.getDtCriacao().getTime()) : null)
                                .dtAtualizacao(p.getDtAtualizacao() != null ? new java.sql.Date(p.getDtAtualizacao().getTime()) : null)
                                .duplicataId(d.getId())
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
                        .valor(p.getValor())
                        .desconto(p.getDesconto())
                        .multa(p.getMulta())
                        .valorTotal(p.getValorTotal())
                        .dtVencimento(p.getDtVencimento() != null ? new java.sql.Date(p.getDtVencimento().getTime()) : null)
                        .dtCriacao(p.getDtCriacao() != null ? new java.sql.Date(p.getDtCriacao().getTime()) : null)
                        .dtAtualizacao(p.getDtAtualizacao() != null ? new java.sql.Date(p.getDtAtualizacao().getTime()) : null)
                        .duplicataId(duplicata.getId())
                        .build()
        ).collect(Collectors.toList());

        return DuplicataResponseDTO.builder()
                .id(duplicata.getId())
                .descricao(duplicata.getDescricao())
                .valor(duplicata.getValor())
                .desconto(duplicata.getDesconto())
                .multa(duplicata.getMulta())
                .valorTotal(duplicata.getValorTotal())
                .dtCriacao(duplicata.getDtCriacao() != null ? new java.sql.Date(duplicata.getDtCriacao().getTime()) : null)
                .dtAtualizacao(duplicata.getDtAtualizacao() != null ? new java.sql.Date(duplicata.getDtAtualizacao().getTime()) : null)
                .parcelas(parcelasDTO)
                .build();
    }

    @Override
    @Transactional
    public void salvarDuplicata(DuplicataRequestDTO dto) {
        Duplicata duplicata = new Duplicata();
        duplicata.setDescricao(dto.getDescricao());
        duplicata.setValor(dto.getValor());
        duplicata.setDesconto(dto.getDesconto());
        duplicata.setMulta(dto.getMulta());
        duplicata.setValorTotal(dto.getValorTotal());
        duplicata.setDtCriacao(new java.util.Date());

        duplicataRepository.save(duplicata);

        if (dto.getParcelas() != null) {
            for (var parcDto : dto.getParcelas()) {
                Parcela parcela = new Parcela();
                parcela.setDuplicata(duplicata);
                parcela.setValor(parcDto.getValor());
                parcela.setDesconto(parcDto.getDesconto());
                parcela.setMulta(parcDto.getMulta());
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
        duplicata.setValorTotal(dto.getValorTotal());
        duplicata.setDtAtualizacao(new java.util.Date());

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
