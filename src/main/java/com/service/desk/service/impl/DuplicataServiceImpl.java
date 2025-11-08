package com.service.desk.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.service.desk.dto.DuplicataDiaResponseDTO;
import com.service.desk.dto.DuplicataDiaVencidoResponseDTO;
import com.service.desk.dto.DuplicataRequestDTO;
import com.service.desk.dto.DuplicataResponseDTO;
import com.service.desk.dto.NotaFiscalResponseDTO;
import com.service.desk.dto.ParcelamentoDTO;
import com.service.desk.dto.RelatorioContasAbertasResponseDTO;
import com.service.desk.entidade.Duplicata;
import com.service.desk.entidade.NotaFiscal;
import com.service.desk.entidade.Parcela;
import com.service.desk.enumerator.MensagemEnum;
import com.service.desk.exceptions.NegocioException;
import com.service.desk.repository.DuplicataRepository;
import com.service.desk.repository.FilialRepository;
import com.service.desk.repository.FormaPagamentoRepository;
import com.service.desk.repository.FornecedorRepository;
import com.service.desk.repository.NotaFiscalRepository;
import com.service.desk.repository.ParcelaPrevistaNotaRepository;
import com.service.desk.repository.ParcelaRepository;
import com.service.desk.service.service.DuplicataService;
import com.service.desk.utils.FuxoCaixaUtils;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

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
    
    @Autowired
    private ParcelaPrevistaNotaRepository parcelaPrevistaNotaRepository;
    
    @Autowired
    private FornecedorRepository fornecedorRepository;
    
    @Autowired
    private FilialRepository filialRepository;

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
                .fornecedorId(Objects.nonNull(d.getFornecedor()) ? d.getFornecedor().getId() : null)
                .filialId(Objects.nonNull(d.getFilial()) ? d.getFilial().getId() : null)
                .dsFilial(Objects.nonNull(d.getFilial()) ? d.getFilial().getNome() : null)
                .dsFornecedor(Objects.nonNull(d.getFornecedor()) ? d.getFornecedor().getNome() : null)
                .dtCriacao(d.getDtCriacao() != null ? d.getDtCriacao() : null)
                .dtAtualizacao(d.getDtAtualizacao() != null ? d.getDtAtualizacao() : null)
                .parcelas(d.getParcelas() != null ? d.getParcelas().stream().map(p ->
                        ParcelamentoDTO.builder()
                                .id(p.getId())
                                .numeroParcela(p.getNumeroParcela())
                                .valorTotal(p.getValorTotal())
                                .dtVencimento(p.getDtVencimento() != null ? p.getDtVencimento() : null)
                                .dtCriacao(p.getDtCriacao() != null ? p.getDtCriacao() : null)
                                .dtAtualizacao(p.getDtAtualizacao() != null ? p.getDtAtualizacao() : null)
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
    public Page<DuplicataResponseDTO> listarDuplicatasPaginadas(int pagina, int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho);
        Page<Duplicata> duplicatasPage = duplicataRepository.findAll(pageable);

        return duplicatasPage.map(d -> DuplicataResponseDTO.builder()
                .id(d.getId())
                .descricao(d.getDescricao())
                .valor(d.getValor())
                .desconto(d.getDesconto())
                .multa(d.getMulta())
                .juros(d.getJuros())
                .valorTotal(d.getValorTotal())
                .formaPagamentoId(Objects.nonNull(d.getFormaPagamento()) ? d.getFormaPagamento().getId() : null)
                .fornecedorId(Objects.nonNull(d.getFornecedor()) ? d.getFornecedor().getId() : null)
                .filialId(Objects.nonNull(d.getFilial()) ? d.getFilial().getId() : null)
                .dsFornecedor(Objects.nonNull(d.getFornecedor()) ? d.getFornecedor().getNome() : null)
                .dsFilial(Objects.nonNull(d.getFilial()) ? d.getFilial().getNome() : null)
                .dtCriacao(d.getDtCriacao() != null ? d.getDtCriacao() : null)
                .dtAtualizacao(d.getDtAtualizacao() != null ? d.getDtAtualizacao() : null)
                .parcelas(d.getParcelas() != null ? d.getParcelas().stream().map(p ->
                        ParcelamentoDTO.builder()
                                .id(p.getId())
                                .numeroParcela(p.getNumeroParcela())
                                .valorTotal(p.getValorTotal())
                                .dtVencimento(p.getDtVencimento() != null ? p.getDtVencimento() : null)
                                .dtCriacao(p.getDtCriacao() != null ? p.getDtCriacao() : null)
                                .dtAtualizacao(p.getDtAtualizacao() != null ? p.getDtAtualizacao() : null)
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
                .build());
    }

    @Override 
    public Page<DuplicataResponseDTO> buscarDuplicatasPorNumeroPaginadas(String numero, int pagina, int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho);
        Page<Duplicata> duplicatasPage = duplicataRepository.findByDescricaoContaining(numero, pageable);

        return duplicatasPage.map(d -> DuplicataResponseDTO.builder()
                .id(d.getId())
                .descricao(d.getDescricao())
                .valor(d.getValor())
                .desconto(d.getDesconto())
                .multa(d.getMulta())
                .juros(d.getJuros())
                .valorTotal(d.getValorTotal())
                .formaPagamentoId(Objects.nonNull(d.getFormaPagamento()) ? d.getFormaPagamento().getId() : null)
                .fornecedorId(Objects.nonNull(d.getFornecedor()) ? d.getFornecedor().getId() : null)
                .filialId(Objects.nonNull(d.getFilial()) ? d.getFilial().getId() : null)
                .dsFilial(Objects.nonNull(d.getFilial()) ? d.getFilial().getNome() : null)
                .dsFornecedor(Objects.nonNull(d.getFornecedor()) ? d.getFornecedor().getNome() : null)
                .dtCriacao(d.getDtCriacao() != null ? d.getDtCriacao() : null)
                .dtAtualizacao(d.getDtAtualizacao() != null ? d.getDtAtualizacao() : null)
                .parcelas(d.getParcelas() != null ? d.getParcelas().stream().map(p ->
                        ParcelamentoDTO.builder()
                                .id(p.getId())
                                .numeroParcela(p.getNumeroParcela())
                                .valorTotal(p.getValorTotal())
                                .dtVencimento(p.getDtVencimento() != null ? p.getDtVencimento() : null)
                                .dtCriacao(p.getDtCriacao() != null ? p.getDtCriacao() : null)
                                .dtAtualizacao(p.getDtAtualizacao() != null ? p.getDtAtualizacao() : null)
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
                .build());
    }

    @Override
    public DuplicataResponseDTO buscarDuplicataPorId(Long id) {
        var duplicata = duplicataRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Duplicata não encontrada para id " + id));

        List<ParcelamentoDTO> parcelasDTO = duplicata.getParcelas().stream().map(p ->
                ParcelamentoDTO.builder()
                        .id(p.getId())
                        .numeroParcela(p.getNumeroParcela())
                        .valorTotal(p.getValorTotal())
                        .dtVencimento(p.getDtVencimento() != null ? p.getDtVencimento() : null)
                        .dtCriacao(p.getDtCriacao() != null ? p.getDtCriacao() : null)
                        .dtAtualizacao(p.getDtAtualizacao() != null ? p.getDtAtualizacao() : null)
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
                .fornecedorId(duplicata.getFornecedor().getId())
                .filialId(Objects.nonNull(duplicata.getFilial()) ? duplicata.getFilial().getId() : null)
                .dsFilial(Objects.nonNull(duplicata.getFilial()) ? duplicata.getFilial().getNome() : null)
                .dsFornecedor(duplicata.getFornecedor().getNome())
                .dtCriacao(duplicata.getDtCriacao() != null ? duplicata.getDtCriacao() : null)
                .dtAtualizacao(duplicata.getDtAtualizacao() != null ? duplicata.getDtAtualizacao() : null)
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
        var fornecedor = fornecedorRepository.findById(dto.getFornecedorId()).orElseThrow();
        var filial = filialRepository.findById(dto.getFilialId()).orElseThrow();
        duplicata.setFornecedor(fornecedor);
        duplicata.setFormaPagamento(formaPagamento);
        duplicata.setFilial(filial);
        duplicata.setDtCriacao(LocalDate.now());
        
        // Notas fiscais associadas
        if (dto.getNotasFiscais() != null && !dto.getNotasFiscais().isEmpty()) {
            List<NotaFiscal> notas = dto.getNotasFiscais().stream()
                .map(nfDto -> {
                    NotaFiscal nf = notaFiscalRepository.findById(nfDto.getId()).orElseThrow();
                    if (!CollectionUtils.isEmpty(nf.getParcelasPrevistas())) {
                    	parcelaPrevistaNotaRepository.deleteAll(nf.getParcelasPrevistas());
                    }
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
                parcela.setNumeroParcela(parcDto.getNumeroParcela());
                parcela.setValorTotal(parcDto.getValorTotal());
                parcela.setDtVencimento(parcDto.getDtVencimento());
                parcela.setDtCriacao(LocalDate.now());
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
        var fornecedor = fornecedorRepository.findById(dto.getFornecedorId()).orElseThrow();
        var filial = filialRepository.findById(dto.getFilialId()).orElseThrow();
        duplicata.setFornecedor(fornecedor);
        duplicata.setFormaPagamento(formaPagamento);
        duplicata.setFilial(filial);
        duplicata.setDtAtualizacao(LocalDate.now());
        

        // Atualizar notas fiscais associadas
        List<NotaFiscal> novasNotas = 
        	    (dto.getNotasFiscais() != null && !dto.getNotasFiscais().isEmpty())
        	        ? dto.getNotasFiscais().stream()
        	            .map(nfDto -> notaFiscalRepository.findById(nfDto.getId())
        	                .orElseThrow(() -> new NegocioException(MensagemEnum.MSGE010.getKey())))
        	            .peek(nf -> {
        	                // Se houver parcelas previstas, exclui antes de associar à duplicata
        	                if (!CollectionUtils.isEmpty(nf.getParcelasPrevistas())) {
        	                    parcelaPrevistaNotaRepository.deleteAll(nf.getParcelasPrevistas());
        	                }
        	                nf.setDuplicata(duplicata);
        	            })
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
                    	existente.setNumeroParcela(parcelaDto.getNumeroParcela());
                        existente.setDtVencimento(parcelaDto.getDtVencimento());
                        existente.setValorTotal(parcelaDto.getValorTotal());
                        parcelaRepository.save(existente);
                    }
                } else {
                    var nova = Parcela.builder()
                            .duplicata(duplicata)
                            .numeroParcela(parcelaDto.getNumeroParcela())
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
    
    @Override
    public List<DuplicataResponseDTO> buscarDuplicataPorDescricao(String descricao) {
        List<Duplicata> duplicatas = duplicataRepository.findByDescricaoContaining(descricao);
        return duplicatas.stream()
                         .map(this::mapToDTO)
                         .collect(Collectors.toList());
    }
    
    @Override
    public List<DuplicataDiaResponseDTO> obterContasPagarDia() {
        var hoje = LocalDate.now();

        var parcelas = parcelaRepository.findByDtVencimento(hoje);
        var listaParcelas = new ArrayList<>(parcelas.stream().map(p -> DuplicataDiaResponseDTO.builder()
                .id(p.getDuplicata().getId())
                .fornecedor(p.getDuplicata().getFornecedor().getNome())
                .filial(p.getDuplicata().getFilial().getNome())
                .identificacaoFornecedor(p.getDuplicata().getFornecedor().getIdentificacao())
                .descricao(Objects.nonNull(p.getNumeroParcela()) ?  p.getNumeroParcela() : p.getDuplicata().getDescricao())
                .valor(p.getValorTotal())
                .situacao(p.getStatus() == null ? "Em Aberto" : p.getStatus().getDescricao())
                .dtVencimento(p.getDtVencimento())
                .dtVendimentoFormatada(FuxoCaixaUtils.formatarData(p.getDtVencimento()))
                .valorFormatado(FuxoCaixaUtils.formatarValorBR(p.getValorTotal()))
                .build())
            .toList());

        var parcelasPrevistas = parcelaPrevistaNotaRepository.findByDtVencimentoPrevisto(hoje);
        var listaParcelasPrevistas = parcelasPrevistas.stream().map(p -> DuplicataDiaResponseDTO.builder()
                .id(p.getId())
                .descricao(p.getNotaFiscal().getNumero()+" - " + p.getNumeroParcela())
                .fornecedor(p.getNotaFiscal().getFornecedor().getNome())
                .filial(p.getNotaFiscal().getFilial().getNome())
                .identificacaoFornecedor(p.getNotaFiscal().getFornecedor().getIdentificacao())
                .valor(p.getValorPrevisto())
                .situacao("Em Aberto")
                .dtVencimento(p.getDtVencimentoPrevisto())
                .dtVendimentoFormatada(FuxoCaixaUtils.formatarData(p.getDtVencimentoPrevisto()))
                .build())
            .toList();

        listaParcelas.addAll(listaParcelasPrevistas);
        
        return listaParcelas;
    }
        
    @Override
    public List<DuplicataDiaResponseDTO> obterContasPagarVencida() {
        var hoje = LocalDate.now();

        var parcelas = parcelaRepository.findByDtVencimentoBefore(hoje);
        var listaParcelas = new ArrayList<>(parcelas.stream().map(p -> DuplicataDiaResponseDTO.builder()
                .id(p.getDuplicata().getId())
                .identificacaoFornecedor(p.getDuplicata().getFornecedor().getIdentificacao())
                .fornecedor(p.getDuplicata().getFornecedor().getNome())
                .filial(p.getDuplicata().getFilial().getNome())
                .descricao(Objects.nonNull(p.getNumeroParcela()) ?  p.getNumeroParcela() : p.getDuplicata().getDescricao())
                .valor(p.getValorTotal())
                .situacao(p.getStatus() == null ? "Vencida" : p.getStatus().getDescricao())
                .dtVencimento(p.getDtVencimento())
                .dtVendimentoFormatada(FuxoCaixaUtils.formatarData(p.getDtVencimento()))
                .valorFormatado(FuxoCaixaUtils.formatarValorBR(p.getValorTotal()))
                .build())
            .toList());

        var parcelasPrevistas = parcelaPrevistaNotaRepository.findByDtVencimentoPrevistoBefore(hoje);
        var listaParcelasPrevistas = parcelasPrevistas.stream().map(p -> DuplicataDiaResponseDTO.builder()
                .id(p.getId())
                .identificacaoFornecedor(p.getNotaFiscal().getFornecedor().getIdentificacao())
                .fornecedor(p.getNotaFiscal().getFornecedor().getNome())
                .filial(p.getNotaFiscal().getFilial().getNome())
                .descricao(p.getNotaFiscal().getNumero()+" - " + p.getNumeroParcela())
                .valor(p.getValorPrevisto())
                .situacao("Prevista Vencida")
                .dtVencimento(p.getDtVencimentoPrevisto())
                .dtVendimentoFormatada(FuxoCaixaUtils.formatarData(p.getDtVencimentoPrevisto()))
                .valorFormatado(FuxoCaixaUtils.formatarValorBR(p.getValorPrevisto()))
                .build())
            .toList();

        listaParcelas.addAll(listaParcelasPrevistas);
        
        listaParcelas.sort(Comparator.comparing(DuplicataDiaResponseDTO::getDtVencimento));

        return listaParcelas;
    }
    
    @Override
    public DuplicataDiaVencidoResponseDTO obterContasPagarDiaAndVencidas() {
    	return DuplicataDiaVencidoResponseDTO.builder()
    			.dia(obterContasPagarDia())
    			.vencidos(obterContasPagarVencida())
    			.build();
    }

    private DuplicataResponseDTO mapToDTO(Duplicata duplicata) {
        List<ParcelamentoDTO> parcelasDTO = duplicata.getParcelas().stream()
                .map(p -> ParcelamentoDTO.builder()
                        .id(p.getId())
                        .numeroParcela(p.getNumeroParcela())
                        .valorTotal(p.getValorTotal())
                        .dtVencimento(p.getDtVencimento() != null ? p.getDtVencimento() : null)
                        .dtCriacao(p.getDtCriacao() != null ? p.getDtCriacao() : null)
                        .dtAtualizacao(p.getDtAtualizacao() != null ? p.getDtAtualizacao() : null)
                        .duplicataId(duplicata.getId())
                        .build())
                .collect(Collectors.toList());

        List<NotaFiscalResponseDTO> notaFiscalResponseDTO = duplicata.getNotasFiscais().stream()
                .map(nf -> NotaFiscalResponseDTO.builder()
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
                        .build())
                .collect(Collectors.toList());

        return DuplicataResponseDTO.builder()
                .id(duplicata.getId())
                .descricao(duplicata.getDescricao())
                .valor(duplicata.getValor())
                .desconto(duplicata.getDesconto())
                .multa(duplicata.getMulta())
                .valorTotal(duplicata.getValorTotal())
                .formaPagamentoId(duplicata.getFormaPagamento().getId())
                .fornecedorId(duplicata.getFornecedor().getId())
                .filialId(duplicata.getFilial().getId())
                .dsFornecedor(duplicata.getFornecedor().getNome())
                .dsFilial(duplicata.getFilial().getNome())
                .dtCriacao(duplicata.getDtCriacao() != null ? duplicata.getDtCriacao() : null)
                .dtAtualizacao(duplicata.getDtAtualizacao() != null ? duplicata.getDtAtualizacao() : null)
                .parcelas(parcelasDTO)
                .notasFiscais(notaFiscalResponseDTO)
                .build();
    }
    
    @Override
    public RelatorioContasAbertasResponseDTO gerarRelatorioContasEmAbertoPorFilial(Long idFilial) {

        var duplicatas = duplicataRepository.findByFilialId(idFilial);

        var parcelasEmAberto = duplicatas.stream()
            .flatMap(d -> d.getParcelas().stream()
                .filter(p -> p.getStatus() == null ||
                            p.getStatus().getDescricao().equalsIgnoreCase("Em Aberto"))
                .map(p -> new Object[] {
                    d.getFornecedor().getNome(),
                    p.getDtVencimento(),
                    p.getValorTotal()
                })
            )
            .collect(Collectors.toList());

        var parcelasPrevistas = parcelaPrevistaNotaRepository.findByNotaFiscalFilialId(idFilial).stream()
            .map(p -> new Object[] {
                p.getNotaFiscal().getFornecedor().getNome(),
                p.getDtVencimentoPrevisto(),
                p.getValorPrevisto()
            })
            .collect(Collectors.toList());

        parcelasEmAberto.addAll(parcelasPrevistas);

        // Agrupa por mês/ano (nome por extenso + ano)
        Map<YearMonth, List<Object[]>> agrupadoPorMes = parcelasEmAberto.stream()
            .collect(Collectors.groupingBy(item -> {
                LocalDate data = (LocalDate) item[1];
                return YearMonth.of(data.getYear(), data.getMonth());
            }, TreeMap::new, Collectors.toList())); // TreeMap mantém ordenação natural crescente

        List<RelatorioContasAbertasResponseDTO.RelatorioMesDTO> meses = new ArrayList<>();
        BigDecimal totalGeral = BigDecimal.ZERO;
        Locale locale = new Locale("pt", "BR");

        for (var entry : agrupadoPorMes.entrySet()) {
            YearMonth ym = entry.getKey();
            String mesExtenso = ym.getMonth().getDisplayName(TextStyle.FULL, locale);
            String mesAno = mesExtenso + "/" + ym.getYear();

            List<Object[]> registrosMes = entry.getValue();

            Map<String, BigDecimal> subtotalPorFornecedor = registrosMes.stream()
                .collect(Collectors.groupingBy(
                    item -> (String) item[0],
                    LinkedHashMap::new,
                    Collectors.mapping(
                        item -> (BigDecimal) item[2],
                        Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                    )
                ));

            List<RelatorioContasAbertasResponseDTO.FornecedorValorDTO> fornecedores =
                subtotalPorFornecedor.entrySet().stream()
                    .map(e -> RelatorioContasAbertasResponseDTO.FornecedorValorDTO.builder()
                            .fornecedor(e.getKey())
                            .valor(e.getValue())
                            .build())
                    .toList();

            BigDecimal subtotal = fornecedores.stream()
                .map(RelatorioContasAbertasResponseDTO.FornecedorValorDTO::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            totalGeral = totalGeral.add(subtotal);

            meses.add(RelatorioContasAbertasResponseDTO.RelatorioMesDTO.builder()
                    .mesAno(mesAno)
                    .fornecedores(fornecedores)
                    .subtotal(subtotal)
                    .build());
        }

        return RelatorioContasAbertasResponseDTO.builder()
                .meses(meses)
                .totalGeral(totalGeral)
                .build();
    }
}
