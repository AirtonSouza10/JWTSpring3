package com.service.desk.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.service.desk.dto.BaixaParcelaRequestDTO;
import com.service.desk.dto.DuplicataBuscaGeralDTO;
import com.service.desk.dto.DuplicataDiaResponseDTO;
import com.service.desk.dto.DuplicataDiaVencidoResponseDTO;
import com.service.desk.dto.DuplicataRequestDTO;
import com.service.desk.dto.DuplicataResponseDTO;
import com.service.desk.dto.FiltroRelatorioCustomizadoDTO;
import com.service.desk.dto.NotaFiscalResponseDTO;
import com.service.desk.dto.NotaFiscalResumoDTO;
import com.service.desk.dto.ParcelaBuscaGeralDTO;
import com.service.desk.dto.ParcelaResponseDTO;
import com.service.desk.dto.ParcelaResumoDTO;
import com.service.desk.dto.ParcelaUpdateRequestDTO;
import com.service.desk.dto.ParcelamentoDTO;
import com.service.desk.dto.RelatorioContasAbertasResponseDTO;
import com.service.desk.dto.RelatorioCustomizadoResponseDTO;
import com.service.desk.dto.RelatorioParcelasPagasPorTipoDTO;
import com.service.desk.entidade.Duplicata;
import com.service.desk.entidade.NotaFiscal;
import com.service.desk.entidade.Parcela;
import com.service.desk.entidade.StatusConta;
import com.service.desk.enumerator.MensagemEnum;
import com.service.desk.enumerator.StatusContaEnum;
import com.service.desk.exceptions.NegocioException;
import com.service.desk.repository.DuplicataRepository;
import com.service.desk.repository.FilialRepository;
import com.service.desk.repository.FormaPagamentoRepository;
import com.service.desk.repository.FornecedorRepository;
import com.service.desk.repository.NotaFiscalRepository;
import com.service.desk.repository.ParcelaPrevistaNotaRepository;
import com.service.desk.repository.ParcelaRepository;
import com.service.desk.repository.StatusContaRepository;
import com.service.desk.repository.TipoPagamentoRepository;
import com.service.desk.repository.TipoRepository;
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
    
    @Autowired
    private TipoPagamentoRepository tipoPagamentoRepository;
    
    @Autowired
    private StatusContaRepository statusContaRepository;
    
    @Autowired
    private TipoRepository tipoRepository;
    
    @Override
    public List<DuplicataResponseDTO> listarDuplicatas() {
        var duplicatas = duplicataRepository.findAll();

        return duplicatas.stream().map(d -> {

            var parcelas = parcelaRepository.findByDuplicataId(d.getId());
            var notasFiscais = notaFiscalRepository.findByDuplicataId(d.getId());

            return DuplicataResponseDTO.builder()
                    .id(d.getId())
                    .descricao(d.getDescricao())
                    .valor(d.getValor())
                    .desconto(d.getDesconto())
                    .multa(d.getMulta())
                    .juros(d.getJuros())
                    .valorTotal(d.getValorTotal())
                    .formaPagamentoId(d.getFormaPagamento() != null ? d.getFormaPagamento().getId() : null)
                    .fornecedorId(d.getFornecedor() != null ? d.getFornecedor().getId() : null)
                    .filialId(d.getFilial() != null ? d.getFilial().getId() : null)
                    .dsFilial(d.getFilial() != null ? d.getFilial().getNome() : null)
                    .dsFornecedor(d.getFornecedor() != null ? d.getFornecedor().getNome() : null)
                    .tipoId(d.getTipo() != null ? d.getTipo().getId() : null)
                    .dsTipo(d.getTipo() != null ? d.getTipo().getDescricao() : null)
                    .dtCriacao(d.getDtCriacao())
                    .dtAtualizacao(d.getDtAtualizacao())

                    .parcelas(parcelas.stream().map(p ->
                            ParcelamentoDTO.builder()
                                    .id(p.getId())
                                    .numeroParcela(p.getNumeroParcela())
                                    .valorTotal(p.getValorTotal())
                                    .dtVencimento(p.getDtVencimento())
                                    .dtCriacao(p.getDtCriacao())
                                    .dtAtualizacao(p.getDtAtualizacao())
                                    .duplicataId(d.getId())
                                    .build()
                    ).toList())

                    .notasFiscais(notasFiscais.stream().map(nf ->
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
                    ).toList())

                    .build();

        }).toList();
    }
    
    @Override   
    public Page<DuplicataResponseDTO> listarDuplicatasPaginadas(int pagina, int tamanho) {

        Pageable pageable = PageRequest.of(pagina, tamanho);
        Page<Duplicata> duplicatasPage = duplicataRepository.findAll(pageable);

        return duplicatasPage.map(d -> {

            var parcelas = parcelaRepository.findByDuplicataId(d.getId());
            var notasFiscais = notaFiscalRepository.findByDuplicataId(d.getId());

            return DuplicataResponseDTO.builder()
                    .id(d.getId())
                    .descricao(d.getDescricao())
                    .valor(d.getValor())
                    .desconto(d.getDesconto())
                    .multa(d.getMulta())
                    .juros(d.getJuros())
                    .valorTotal(d.getValorTotal())
                    .formaPagamentoId(d.getFormaPagamento() != null ? d.getFormaPagamento().getId() : null)
                    .fornecedorId(d.getFornecedor() != null ? d.getFornecedor().getId() : null)
                    .filialId(d.getFilial() != null ? d.getFilial().getId() : null)
                    .dsFornecedor(d.getFornecedor() != null ? d.getFornecedor().getNome() : null)
                    .dsFilial(d.getFilial() != null ? d.getFilial().getNome() : null)
                    .tipoId(d.getTipo() != null ? d.getTipo().getId() : null)
                    .dsTipo(d.getTipo() != null ? d.getTipo().getDescricao() : null)
                    .dtCriacao(d.getDtCriacao())
                    .dtAtualizacao(d.getDtAtualizacao())

                    .parcelas(parcelas.stream().map(p ->
                            ParcelamentoDTO.builder()
                                    .id(p.getId())
                                    .numeroParcela(p.getNumeroParcela())
                                    .valorTotal(p.getValorTotal())
                                    .dtVencimento(p.getDtVencimento())
                                    .dtCriacao(p.getDtCriacao())
                                    .dtAtualizacao(p.getDtAtualizacao())
                                    .duplicataId(d.getId())
                                    .build()
                    ).toList())

                    .notasFiscais(notasFiscais.stream().map(nf ->
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
                    ).toList())

                    .build();
        });
    }

    @Override 
    public Page<DuplicataResponseDTO> buscarDuplicatasPorNumeroPaginadas(String numero, int pagina, int tamanho) {

        Pageable pageable = PageRequest.of(pagina, tamanho);
        Page<Duplicata> page = duplicataRepository.findByDescricaoContaining(numero, pageable);

        return page.map(d -> {

            var parcelas = parcelaRepository.findByDuplicataId(d.getId());
            var notasFiscais = notaFiscalRepository.findByDuplicataId(d.getId());

            return DuplicataResponseDTO.builder()
                    .id(d.getId())
                    .descricao(d.getDescricao())
                    .valor(d.getValor())
                    .desconto(d.getDesconto())
                    .multa(d.getMulta())
                    .juros(d.getJuros())
                    .valorTotal(d.getValorTotal())
                    .formaPagamentoId(d.getFormaPagamento() != null ? d.getFormaPagamento().getId() : null)
                    .fornecedorId(d.getFornecedor() != null ? d.getFornecedor().getId() : null)
                    .filialId(d.getFilial() != null ? d.getFilial().getId() : null)
                    .dsFilial(d.getFilial() != null ? d.getFilial().getNome() : null)
                    .dsFornecedor(d.getFornecedor() != null ? d.getFornecedor().getNome() : null)
                    .tipoId(d.getTipo() != null ? d.getTipo().getId() : null)
                    .dsTipo(d.getTipo() != null ? d.getTipo().getDescricao() : null)
                    .dtCriacao(d.getDtCriacao())
                    .dtAtualizacao(d.getDtAtualizacao())

                    .parcelas(parcelas.stream().map(p ->
                            ParcelamentoDTO.builder()
                                    .id(p.getId())
                                    .numeroParcela(p.getNumeroParcela())
                                    .valorTotal(p.getValorTotal())
                                    .dtVencimento(p.getDtVencimento())
                                    .dtCriacao(p.getDtCriacao())
                                    .dtAtualizacao(p.getDtAtualizacao())
                                    .duplicataId(d.getId())
                                    .build()
                    ).toList())

                    .notasFiscais(notasFiscais.stream().map(nf ->
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
                    ).toList())

                    .build();
        });
    }

    @Override
    public DuplicataResponseDTO buscarDuplicataPorId(Long id) {

        var duplicata = duplicataRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Duplicata não encontrada para id " + id));

        var parcelas = parcelaRepository.findByDuplicataId(id);
        var notasFiscais = notaFiscalRepository.findByDuplicataId(id);

        List<ParcelamentoDTO> parcelasDTO = parcelas.stream().map(p ->
                ParcelamentoDTO.builder()
                        .id(p.getId())
                        .numeroParcela(p.getNumeroParcela())
                        .valorTotal(p.getValorTotal())
                        .dtVencimento(p.getDtVencimento())
                        .dtCriacao(p.getDtCriacao())
                        .dtAtualizacao(p.getDtAtualizacao())
                        .duplicataId(duplicata.getId())
                        .build()
        ).toList();

        List<NotaFiscalResponseDTO> notasDTO = notasFiscais.stream().map(nf ->
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
        ).toList();

        return DuplicataResponseDTO.builder()
                .id(duplicata.getId())
                .descricao(duplicata.getDescricao())
                .valor(duplicata.getValor())
                .desconto(duplicata.getDesconto())
                .multa(duplicata.getMulta())
                .valorTotal(duplicata.getValorTotal())
                .formaPagamentoId(duplicata.getFormaPagamento() != null ? duplicata.getFormaPagamento().getId() : null)
                .fornecedorId(duplicata.getFornecedor() != null ? duplicata.getFornecedor().getId() : null)
                .filialId(duplicata.getFilial() != null ? duplicata.getFilial().getId() : null)
                .dsFilial(duplicata.getFilial() != null ? duplicata.getFilial().getNome() : null)
                .tipoId(duplicata.getTipo() != null ? duplicata.getTipo().getId() : null)
                .dsTipo(duplicata.getTipo() != null ? duplicata.getTipo().getDescricao() : null)
                .dsFornecedor(duplicata.getFornecedor() != null ? duplicata.getFornecedor().getNome() : null)
                .dtCriacao(duplicata.getDtCriacao())
                .dtAtualizacao(duplicata.getDtAtualizacao())
                .parcelas(parcelasDTO)
                .notasFiscais(notasDTO)
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
        var tipo = tipoRepository.findById(dto.getTipoId()).orElseThrow();
        duplicata.setFornecedor(fornecedor);
        duplicata.setFormaPagamento(formaPagamento);
        duplicata.setFilial(filial);
        duplicata.setTipo(tipo);
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
                parcela.setStatus(StatusConta.builder().id(StatusContaEnum.EM_ABERTO.getId()).descricao(StatusContaEnum.EM_ABERTO.getDescricao()).build());
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
        
		boolean existeParcelaPaga = parcelaRepository.existsByDuplicataIdAndStatusId(duplicata.getId(),
				StatusContaEnum.PAGO.getId());

		var parcelasSalvas = parcelaRepository.findByDuplicataId(duplicata.getId());
		int qtdAtual = parcelasSalvas.size();
		int qtdNova = dto.getParcelas() != null ? dto.getParcelas().size() : 0;

		if (existeParcelaPaga && qtdAtual != qtdNova) {
		    throw new NegocioException(MensagemEnum.MSGE017.getKey());
		}
		
        duplicata.setDescricao(dto.getDescricao());
        duplicata.setValor(dto.getValor());
        duplicata.setDesconto(dto.getDesconto());
        duplicata.setMulta(dto.getMulta());
        duplicata.setJuros(dto.getJuros());
        duplicata.setValorTotal(dto.getValorTotal());
        var formaPagamento = formaPagamentoRepository.findById(dto.getFormaPagamentoId()).orElseThrow(() -> new NegocioException(MensagemEnum.MSGE010.getKey()));
        var fornecedor = fornecedorRepository.findById(dto.getFornecedorId()).orElseThrow();
        var filial = filialRepository.findById(dto.getFilialId()).orElseThrow();
        var tipo = tipoRepository.findById(dto.getTipoId()).orElseThrow();
        duplicata.setFornecedor(fornecedor);
        duplicata.setFormaPagamento(formaPagamento);
        duplicata.setFilial(filial);
        duplicata.setTipo(tipo);
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

        parcelaRepository.deleteByDuplicataId(id);

        duplicataRepository.delete(duplicata);
    }
    
    @Override
    public List<DuplicataResponseDTO> buscarDuplicataPorDescricao(String descricao) {
        List<Duplicata> duplicatas = duplicataRepository.findByDescricaoContainingIgnoreCase(descricao);
        return duplicatas.stream()
                         .map(this::mapToDTO)
                         .collect(Collectors.toList());
    }
    
    @Override
    public List<DuplicataDiaResponseDTO> obterContasPagarDia() {
        var hoje = LocalDate.now();

        var parcelas = parcelaRepository.findByDtVencimento(hoje);
        var listaParcelas = new ArrayList<>(parcelas.stream()
                .filter(p -> statusNotPago(p))
        		.map(p -> DuplicataDiaResponseDTO.builder()
                .id(p.getId())
                .fornecedor(p.getDuplicata().getFornecedor().getNome())
                .filial(p.getDuplicata().getFilial().getNome())
                .identificacaoFornecedor(p.getDuplicata().getFornecedor().getIdentificacao())
                .descricao(Objects.nonNull(p.getNumeroParcela()) && !p.getNumeroParcela().trim().isEmpty() ?  p.getNumeroParcela() : p.getDuplicata().getDescricao())
                .valor(p.getValorTotal())
                .situacao(emAberto(p)  ? "Em Aberto" : p.getStatus().getDescricao())
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
                .isPrevista(true)
                .build())
            .toList();

        listaParcelas.addAll(listaParcelasPrevistas);
        
        return listaParcelas;
    }

	private boolean statusNotPago(Parcela p) {
		return Objects.isNull(p.getStatus()) 
				|| (!StatusContaEnum.CANCELADO.getId().equals(p.getStatus().getId()) &&
					!StatusContaEnum.PAGO.getId().equals(p.getStatus().getId()));
	}

	private boolean emAberto(Parcela p) {
		return Objects.isNull(p.getStatus()) || StatusContaEnum.EM_ABERTO.getId().equals(p.getStatus().getId());
	}
        
    @Override
    public List<DuplicataDiaResponseDTO> obterContasPagarVencida() {
        var hoje = LocalDate.now();

        var parcelas = parcelaRepository.findByDtVencimentoBefore(hoje);
        var listaParcelas = new ArrayList<>(parcelas.stream()
        		.filter(p -> statusNotPago(p))
        		.map(p -> DuplicataDiaResponseDTO.builder()
                .id(p.getId())
                .identificacaoFornecedor(p.getDuplicata().getFornecedor().getIdentificacao())
                .fornecedor(p.getDuplicata().getFornecedor().getNome())
                .filial(p.getDuplicata().getFilial().getNome())
                .descricao(Objects.nonNull(p.getNumeroParcela()) && !p.getNumeroParcela().trim().isEmpty() ?  p.getNumeroParcela() : p.getDuplicata().getDescricao())
                .valor(p.getValorTotal())
                .situacao(emAberto(p) ? "Vencida" : p.getStatus().getDescricao())
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
                .isPrevista(true)
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
        var filial = filialRepository.findById(idFilial).orElseThrow();

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
            String mesAno = (mesExtenso + "/" + ym.getYear()).toUpperCase();

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
                            .valorFormatado(FuxoCaixaUtils.formatarValorBR(e.getValue()))
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
                    .subtotalformatado(FuxoCaixaUtils.formatarValorBR(subtotal))
                    .build());
        }

        return RelatorioContasAbertasResponseDTO.builder()
        		.filial(filial.getNome())
                .meses(meses)
                .totalGeral(totalGeral)
                .totalGeralFormatado(FuxoCaixaUtils.formatarValorBR(totalGeral))
                .build();
    }
    
    @Override
    @Transactional
    public void baixarParcela(BaixaParcelaRequestDTO dto) {
    	var parcela = parcelaRepository.findById(dto.getId());
    	if(parcela.isPresent()) {
    		var parcelaDuplicata = parcela.get();
    		parcelaDuplicata.setDtAtualizacao(LocalDate.now());
    		parcelaDuplicata.setDtBaixa(LocalDate.now());
    		parcelaDuplicata.setDtPagamento(dto.getDtPagamento());
    		parcelaDuplicata.setObservacao(dto.getObservacao());
    		parcelaDuplicata.setValorPago(dto.getValorPago());
    		var status = statusContaRepository.findById(StatusContaEnum.PAGO.getId()).orElseThrow();
    		parcelaDuplicata.setStatus(status);
    		if(Objects.nonNull(dto.getTipoPagamentoId())) {
    			var tipoPagamento = tipoPagamentoRepository.findById(dto.getTipoPagamentoId());
    			parcelaDuplicata.setTipoPagamento(tipoPagamento.get());
 
    		}
    		parcelaRepository.save(parcelaDuplicata);
    	}
    }
    
    @Override
    @Transactional
    public ParcelaResponseDTO buscarParcelaPorId(Long id) {
    	var parcela = parcelaRepository.findById(id).orElseThrow();
    	return ParcelaResponseDTO.builder()
    			.valor(parcela.getValorTotal())
    			.observacao(parcela.getObservacao())
    			.tipoPagamentoId(Objects.nonNull(parcela.getTipoPagamento()) ? parcela.getTipoPagamento().getId() : null)
    			.dsTipoPagamento(Objects.nonNull(parcela.getTipoPagamento()) ? parcela.getTipoPagamento().getDescricao() : null)
    			.build();
    }
    
    @Override
    public List<RelatorioParcelasPagasPorTipoDTO> gerarRelatorioParcelasPagasPorTipo(
            Long idFilial,
            LocalDate dataInicial,
            LocalDate dataFinal) {

        var filial = filialRepository.findById(idFilial).orElseThrow();

        var duplicatas = duplicataRepository.findByFilialId(idFilial);

        List<Parcela> parcelasPagas = duplicatas.stream()
            .flatMap(d -> d.getParcelas().stream())
            .filter(p ->
                p.getStatus() != null &&
                StatusContaEnum.PAGO.getId().equals(p.getStatus().getId()) &&
                p.getDtPagamento() != null &&
                !p.getDtPagamento().isBefore(dataInicial) &&
                !p.getDtPagamento().isAfter(dataFinal)
            )
            .toList();

        Map<Long, List<Parcela>> agrupadoPorTipo = parcelasPagas.stream()
            .collect(Collectors.groupingBy(
                p -> p.getDuplicata().getTipo().getId(),
                TreeMap::new,
                Collectors.toList()
            ));

        List<RelatorioParcelasPagasPorTipoDTO> resultado = new ArrayList<>();

        for (var entry : agrupadoPorTipo.entrySet()) {

            Long tipoTituloId = entry.getKey();
            var dsTipo = tipoRepository.findById(tipoTituloId).orElseThrow();
            String tipoTitulo = dsTipo.getDescricao();
            List<Parcela> parcelasDoTipo = entry.getValue();

            List<RelatorioParcelasPagasPorTipoDTO.ParcelaPagaDTO> parcelasDTO =
                parcelasDoTipo.stream()
                    .map(p -> RelatorioParcelasPagasPorTipoDTO.ParcelaPagaDTO.builder()
                            .descricao(p.getNumeroParcela() != null ? p.getNumeroParcela() : p.getDuplicata().getDescricao())
                            .dataPagamento(p.getDtPagamento())
                            .dataPagamentoFormatado(FuxoCaixaUtils.formatarData(p.getDtPagamento()))
                            .valorPago(p.getValorPago())
                            .valorPagoFormatado(FuxoCaixaUtils.formatarValorBR(p.getValorPago()))
                            .fornecedor(p.getDuplicata().getFornecedor().getNome())
                            .build()
                    ).toList();

            resultado.add(RelatorioParcelasPagasPorTipoDTO.builder()
                    .filial(filial.getNome())
                    .identificacao(filial.getIdentificacao())
                    .tipoTitulo(tipoTitulo)
                    .parcelas(parcelasDTO)
                    .build());
        }

        return resultado;
    }
    
    @Override
    public List<RelatorioCustomizadoResponseDTO> gerarRelatorioCustomizado(FiltroRelatorioCustomizadoDTO f) {

        Specification<Parcela> spec = Specification.allOf();

        if (f.getIdFilial() != null) {
            spec = spec.and((root, query, cb) ->
                cb.equal(root.get("duplicata").get("filial").get("id"), f.getIdFilial())
            );
        }

        if (f.getIdStatusConta() != null) {
            spec = spec.and((root, query, cb) ->
                cb.equal(root.get("status").get("id"), f.getIdStatusConta())
            );
        }

        if (f.getIdFornecedor() != null) {
            spec = spec.and((root, query, cb) ->
                cb.equal(root.get("duplicata").get("fornecedor").get("id"), f.getIdFornecedor())
            );
        }

        if (f.getIdTipoNota() != null) {
            spec = spec.and((root, query, cb) ->
                cb.equal(
                    root.join("duplicata").join("notasFiscais")
                        .get("tipo").get("id"),
                    f.getIdTipoNota()
                )
            );
        }

        if (f.getIdTipoDuplicata() != null) {
            spec = spec.and((root, query, cb) ->
                cb.equal(root.get("duplicata").get("tipo").get("id"), f.getIdTipoDuplicata())
            );
        }

        if (f.getIdTipoPagamento() != null) {
            spec = spec.and((root, query, cb) ->
                cb.equal(root.get("tipoPagamento").get("id"), f.getIdTipoPagamento())
            );
        }

        if (f.getDataInicial() != null) {
            spec = spec.and((root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("dtVencimento"), f.getDataInicial())
            );
        }

        if (f.getDataFinal() != null) {
            spec = spec.and((root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("dtVencimento"), f.getDataFinal())
            );
        }

        List<Parcela> parcelas = parcelaRepository.findAll(spec);
        
        Collections.sort(parcelas, Comparator.comparing(Parcela::getDtVencimento));

        return parcelas.stream().map(this::toDTO).toList();
    }



    private RelatorioCustomizadoResponseDTO toDTO(Parcela p) {
        Duplicata d = p.getDuplicata();
        var notasFiscaisDuplicata = notaFiscalRepository.findByDuplicataId(d.getId());

        NotaFiscal nf = notasFiscaisDuplicata.isEmpty() ? null : notasFiscaisDuplicata.get(0);

        Long prazo = null;
        if (nf != null && nf.getDtCompra() != null) {
            prazo = ChronoUnit.DAYS.between(nf.getDtCompra(), p.getDtVencimento());
        }

        return RelatorioCustomizadoResponseDTO.builder()
                .loja(d.getFilial() != null ? d.getFilial().getNome() : "")
                .fornecedor(d.getFornecedor() != null ? d.getFornecedor().getNome() : "")
                .tituloDuplicata(d.getDescricao())
                .prazo(prazo)
                .numeroParcela(p.getNumeroParcela() != null && !p.getNumeroParcela().isEmpty()  ? p.getNumeroParcela() : p.getDuplicata().getDescricao())
                .valorParcela(p.getValorTotal())
                .valorParcelaFormatada(FuxoCaixaUtils.formatarValorBR(p.getValorTotal()))
                .dtVencimento(p.getDtVencimento())
                .dtVencimentoFormatada(FuxoCaixaUtils.formatarData(p.getDtVencimento()))
                .status(p.getStatus() != null ? p.getStatus().getDescricao() : "Em Aberto")
                .build();
    }
    
    @Override
    public Page<DuplicataBuscaGeralDTO> buscarGeral(String termo, int pagina, int tamanho) {

        Pageable pageable = PageRequest.of(pagina, tamanho);

        List<Duplicata> duplicatasDescricao =
                duplicataRepository.findByDescricaoContainingIgnoreCase(termo);

        List<Parcela> parcelas =
                parcelaRepository.findByNumeroParcelaContainingIgnoreCase(termo);

        List<NotaFiscal> notas =
                notaFiscalRepository.findByNumeroContainingIgnoreCase(termo);

        Set<Long> duplicataIds = new HashSet<>();

        duplicataIds.addAll(duplicatasDescricao.stream().map(Duplicata::getId).toList());
        duplicataIds.addAll(parcelas.stream().map(p -> p.getDuplicata().getId()).toList());
        duplicataIds.addAll(notas.stream()
                .filter(n -> n.getDuplicata() != null)
                .map(n -> n.getDuplicata().getId())
                .toList()
        );

        Page<Duplicata> pageDuplicatas =
                duplicataRepository.findByIdIn(duplicataIds, pageable);

        return pageDuplicatas.map(duplicata -> {

            String termoLower = termo.toLowerCase();
            List<Parcela> parcelasDaDuplicata = duplicata.getParcelas();

            Set<Long> parcelaIds = new HashSet<>();
            List<ParcelaResumoDTO> parcelasDTO = new ArrayList<>();


            if (duplicata.getDescricao() != null &&
                duplicata.getDescricao().toLowerCase().contains(termoLower)) {

                parcelasDaDuplicata.forEach(p -> {
                    if (parcelaIds.add(p.getId())) {
                        parcelasDTO.add(toParcelaDTO(p));
                    }
                });
            }

            duplicata.getParcelas().stream()
                    .filter(p -> p.getNumeroParcela() != null &&
                                 p.getNumeroParcela().contains(termo))
                    .forEach(p -> {
                        if (parcelaIds.add(p.getId())) {
                            parcelasDTO.add(toParcelaDTO(p));
                        }
                    });

            var notasFiscaisDuplicata = notaFiscalRepository.findByDuplicataId(duplicata.getId());
            boolean matchNota = notasFiscaisDuplicata.stream()
                    .anyMatch(n -> n.getNumero() != null &&
                                   n.getNumero().contains(termo));
            if (matchNota) {
                parcelasDaDuplicata.forEach(p -> {
                    if (parcelaIds.add(p.getId())) {
                        parcelasDTO.add(toParcelaDTO(p));
                    }
                });
            }

            if (parcelasDTO.isEmpty()) {
                parcelasDaDuplicata.forEach(p -> {
                    if (parcelaIds.add(p.getId())) {
                        parcelasDTO.add(toParcelaDTO(p));
                    }
                });
            }

            List<NotaFiscalResumoDTO> notasDTO = notasFiscaisDuplicata
                    .stream()
                    .map(n -> NotaFiscalResumoDTO.builder()
                            .id(n.getId())
                            .numero(n.getNumero())
                            .serie(n.getSerie())
                            .chave(n.getChave())
                            .dtCompra(n.getDtCompra())
                            .valorTotal(n.getValorTotal())
                            .fornecedorId(n.getFornecedor() != null ? n.getFornecedor().getId() : null)
                            .fornecedorNome(n.getFornecedor() != null ? n.getFornecedor().getNome() : null)
                            .filialId(n.getFilial() != null ? n.getFilial().getId() : null)
                            .build()
                    )
                    .toList();

            return DuplicataBuscaGeralDTO.builder()
                    .duplicataId(duplicata.getId())
                    .descricaoDuplicata(duplicata.getDescricao())

                    .filialId(duplicata.getFilial() != null ? duplicata.getFilial().getId() : null)
                    .dsFilial(duplicata.getFilial() != null ? duplicata.getFilial().getNome() : null)

                    .fornecedorId(duplicata.getFornecedor() != null ? duplicata.getFornecedor().getId() : null)
                    .dsFornecedor(duplicata.getFornecedor() != null ? duplicata.getFornecedor().getNome() : null)

                    .parcelas(parcelasDTO)
                    .notasFiscais(notasDTO)
                    .build();
        });
    }

    private ParcelaResumoDTO toParcelaDTO(Parcela p) {
        return ParcelaResumoDTO.builder()
                .id(p.getId())
                .numeroParcela(p.getNumeroParcela())
                .valorTotal(p.getValorTotal())
                .dtVencimento(p.getDtVencimento())
                .dtPagamento(p.getDtPagamento())
                .status(p.getStatus() != null ? p.getStatus().getDescricao() : null)
                .build();
    }

    @Override
    public Page<ParcelaBuscaGeralDTO> buscarGeralParcela(String termo, int pagina, int tamanho) {

        Pageable pageable = PageRequest.of(pagina, tamanho);

        List<Duplicata> duplicatasDescricao =
                duplicataRepository.findByDescricaoContainingIgnoreCase(termo);

        List<Parcela> parcelasMatchNumero =
                parcelaRepository.findByNumeroParcelaContainingIgnoreCase(termo);

        List<NotaFiscal> notas =
                notaFiscalRepository.findByNumeroContainingIgnoreCase(termo);

        Set<Long> duplicataIds = new HashSet<>();
        duplicataIds.addAll(duplicatasDescricao.stream().map(Duplicata::getId).toList());
        duplicataIds.addAll(parcelasMatchNumero.stream().map(p -> p.getDuplicata().getId()).toList());
        duplicataIds.addAll(notas.stream()
                .filter(n -> n.getDuplicata() != null)
                .map(n -> n.getDuplicata().getId())
                .toList()
        );

        List<Duplicata> duplicatas = duplicataRepository.findByIdIn(duplicataIds);

        List<Parcela> parcelasList = duplicatas.stream()
                .flatMap(d -> d.getParcelas().stream())
                .distinct()
                .sorted(Comparator.comparing(Parcela::getDtVencimento))
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), parcelasList.size());
        List<Parcela> pageContent = parcelasList.subList(start, end);

        Page<Parcela> page = new PageImpl<>(pageContent, pageable, parcelasList.size());

        return page.map(parcela -> {
            Duplicata d = parcela.getDuplicata();
            return ParcelaBuscaGeralDTO.builder()
                    .parcelaId(parcela.getId())
                    .numeroParcela(parcela.getNumeroParcela())
                    .valorTotal(parcela.getValorTotal())
                    .dtVencimento(parcela.getDtVencimento())
                    .dtPagamento(parcela.getDtPagamento())
                    .status(parcela.getStatus() != null ? parcela.getStatus().getDescricao() : null)
                    .duplicataId(d.getId())
                    .descricaoDuplicata(d.getDescricao())
                    .fornecedorId(d.getFornecedor() != null ? d.getFornecedor().getId() : null)
                    .fornecedorNome(d.getFornecedor() != null ? d.getFornecedor().getNome() : null)
                    .build();
        });
    }

    @Override
    public Page<ParcelaBuscaGeralDTO> buscarGeralParcelaAtivas(String termo, int pagina, int tamanho) {

        Pageable pageable = PageRequest.of(pagina, tamanho);

        List<Duplicata> duplicatasDescricao =
                duplicataRepository.findByDescricaoContainingIgnoreCase(termo);

        List<Parcela> parcelasMatchNumero =
                parcelaRepository.findByNumeroParcelaContainingIgnoreCase(termo);

        List<NotaFiscal> notas =
                notaFiscalRepository.findByNumeroContainingIgnoreCase(termo);

        Set<Long> duplicataIds = new HashSet<>();
        duplicataIds.addAll(duplicatasDescricao.stream().map(Duplicata::getId).toList());
        duplicataIds.addAll(parcelasMatchNumero.stream().map(p -> p.getDuplicata().getId()).toList());
        duplicataIds.addAll(notas.stream()
                .filter(n -> n.getDuplicata() != null)
                .map(n -> n.getDuplicata().getId())
                .toList()
        );

        List<Duplicata> duplicatas = duplicataRepository.findByIdIn(duplicataIds);

        List<Parcela> parcelasList = duplicatas.stream()
                .flatMap(d -> d.getParcelas().stream())
                .filter(p -> Objects.isNull(p.getStatus()) || (Objects.nonNull(p.getStatus()) && 
                		!StatusContaEnum.CANCELADO.getId().equals(p.getStatus().getId()) && !StatusContaEnum.PAGO.getId().equals(p.getStatus().getId())))
                .distinct()
                .sorted(Comparator.comparing(Parcela::getDtVencimento))
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), parcelasList.size());
        List<Parcela> pageContent = parcelasList.subList(start, end);

        Page<Parcela> page = new PageImpl<>(pageContent, pageable, parcelasList.size());

        return page.map(parcela -> {
            Duplicata d = parcela.getDuplicata();
            return ParcelaBuscaGeralDTO.builder()
                    .parcelaId(parcela.getId())
                    .numeroParcela(parcela.getNumeroParcela())
                    .valorTotal(parcela.getValorTotal())
                    .dtVencimento(parcela.getDtVencimento())
                    .dtPagamento(parcela.getDtPagamento())
                    .status(parcela.getStatus() != null ? parcela.getStatus().getDescricao() : null)
                    .duplicataId(d.getId())
                    .descricaoDuplicata(d.getDescricao())
                    .fornecedorId(d.getFornecedor() != null ? d.getFornecedor().getId() : null)
                    .fornecedorNome(d.getFornecedor() != null ? d.getFornecedor().getNome() : null)
                    .build();
        });
    }

    @Override
    @Transactional
    public void atualizarParcela(Long id, ParcelaUpdateRequestDTO dto) {
    	var parcela = parcelaRepository.findById(id).orElseThrow();
    	if (dto.getStatusId() != null) {
    	    var status = statusContaRepository.findById(dto.getStatusId())
    	            .orElseThrow(() -> new EntityNotFoundException("Status inválido"));
    	    parcela.setStatus(status);
    	}

    	if (dto.getTipoPagamentoId() != null) {
    	    var tipoPagamento = tipoPagamentoRepository.findById(dto.getTipoPagamentoId())
    	            .orElseThrow(() -> new EntityNotFoundException("Tipo de pagamento inválido"));
    	    parcela.setTipoPagamento(tipoPagamento);
    	}
    	parcela.setDtPagamento(dto.getDtPagamento());
    	parcela.setDtVencimento(dto.getDtVencimento());
    	parcela.setNumeroParcela(dto.getNumeroParcela());
    	parcela.setObservacao(dto.getObservacao());
    	parcela.setValorTotal(dto.getValorTotal());
    	parcela.setValorPago(dto.getValorPago());
    	
    	parcelaRepository.save(parcela);
    }
    
}
