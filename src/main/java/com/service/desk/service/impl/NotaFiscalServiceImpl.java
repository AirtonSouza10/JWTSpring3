package com.service.desk.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.service.desk.dto.NotaFiscalRequestDTO;
import com.service.desk.dto.NotaFiscalResponseDTO;
import com.service.desk.dto.ParcelaPrevistaNotaResponseDTO;
import com.service.desk.entidade.NotaFiscal;
import com.service.desk.entidade.ParcelaPrevistaNota;
import com.service.desk.enumerator.MensagemEnum;
import com.service.desk.exceptions.NegocioException;
import com.service.desk.repository.DuplicataRepository;
import com.service.desk.repository.FilialRepository;
import com.service.desk.repository.FormaPagamentoRepository;
import com.service.desk.repository.FornecedorRepository;
import com.service.desk.repository.NotaFiscalRepository;
import com.service.desk.repository.ParcelaPrevistaNotaRepository;
import com.service.desk.repository.PessoaRepository;
import com.service.desk.repository.TipoNotaRepository;
import com.service.desk.service.service.NotaFiscalService;
import com.service.desk.utils.UsuarioLogadoUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotaFiscalServiceImpl implements NotaFiscalService {
	
	@Autowired
    private NotaFiscalRepository notaFiscalRepository;
	@Autowired
    private FornecedorRepository fornecedorRepository;
	@Autowired
    private TipoNotaRepository tipoNotaRepository;
	@Autowired
    private PessoaRepository pessoaRepository;
	@Autowired
    private FilialRepository filialRepository;
	@Autowired
	private ParcelaPrevistaNotaRepository parcelaPrevistaNotaRepository;
	@Autowired
	private FormaPagamentoRepository formaPagamentoRepository;
	@Autowired
	private DuplicataRepository duplicataRepository;

    @Override
    public List<NotaFiscalResponseDTO> listarNotasFiscais() {
        return notaFiscalRepository.findAll().stream()
                .map(nota -> {
                    List<ParcelaPrevistaNotaResponseDTO> parcelasPrevistasDTO = nota.getParcelasPrevistas() != null
                        ? nota.getParcelasPrevistas().stream()
                            .map(parcela -> ParcelaPrevistaNotaResponseDTO.builder()
                                .id(parcela.getId())
                                .dtVencimentoPrevisto(parcela.getDtVencimentoPrevisto())
                                .valorPrevisto(parcela.getValorPrevisto())
                                .build())
                            .toList()
                        : List.of();
                    
                    return NotaFiscalResponseDTO.builder()
                        .id(nota.getId())
                        .numero(nota.getNumero())
                        .serie(nota.getSerie())
                        .chave(nota.getChave())
                        .descricaoObs(nota.getDescricaoObs())
                        .valorTotal(nota.getValorTotal())
                        .valorDesconto(nota.getValorDesconto())
                        .valorIcms(nota.getValorIcms())
                        .valorJuros(nota.getValorJuros())
                        .valorMulta(nota.getValorMulta())
                        .dtCompra(nota.getDtCompra())
                        .fornecedorId(nota.getFornecedor() != null ? nota.getFornecedor().getId() : null)
                        .fornecedorNome(nota.getFornecedor() != null ? nota.getFornecedor().getNome() : null)
                        .tipoNotaId(nota.getTipo() != null ? nota.getTipo().getId() : null)
                        .pessoaId(nota.getPessoa() != null ? nota.getPessoa().getId() : null)
                        .filialId(nota.getFilial() != null ? nota.getFilial().getId() : null)
                        .formaPagamentoId(nota.getFormaPagamento() != null ? nota.getFormaPagamento().getId() : null)
                        .duplicataId(nota.getDuplicata() != null ? nota.getDuplicata().getId() : null)
                        .dsDuplicata(nota.getDuplicata() != null ? nota.getDuplicata().getDescricao() : null)
                        .parcelasPrevistas(parcelasPrevistasDTO)
                        .build();
                })
                .toList();
    }
    
    @Override
    public Page<NotaFiscalResponseDTO> listarNotasFiscaisByNumeroAndFornecedor(String numero, Long fornecedorId, Pageable pageable) {
        Page<NotaFiscal> page;

        if (numero != null && fornecedorId != null) {
            page = notaFiscalRepository.findByNumeroContainingAndFornecedorId(numero, fornecedorId, pageable);
        } else {
            page = notaFiscalRepository.findAll(pageable);
        }

        return page.map(nota -> {
            List<ParcelaPrevistaNotaResponseDTO> parcelasPrevistasDTO = nota.getParcelasPrevistas() != null
                ? nota.getParcelasPrevistas().stream()
                    .map(parcela -> ParcelaPrevistaNotaResponseDTO.builder()
                        .id(parcela.getId())
                        .dtVencimentoPrevisto(parcela.getDtVencimentoPrevisto())
                        .valorPrevisto(parcela.getValorPrevisto())
                        .build())
                    .toList()
                : List.of();

            return NotaFiscalResponseDTO.builder()
                .id(nota.getId())
                .numero(nota.getNumero())
                .serie(nota.getSerie())
                .chave(nota.getChave())
                .descricaoObs(nota.getDescricaoObs())
                .valorTotal(nota.getValorTotal())
                .valorDesconto(nota.getValorDesconto())
                .valorIcms(nota.getValorIcms())
                .valorJuros(nota.getValorJuros())
                .valorMulta(nota.getValorMulta())
                .dtCompra(nota.getDtCompra())
                .fornecedorId(nota.getFornecedor() != null ? nota.getFornecedor().getId() : null)
                .fornecedorNome(nota.getFornecedor() != null ? nota.getFornecedor().getNome() : null)
                .tipoNotaId(nota.getTipo() != null ? nota.getTipo().getId() : null)
                .pessoaId(nota.getPessoa() != null ? nota.getPessoa().getId() : null)
                .filialId(nota.getFilial() != null ? nota.getFilial().getId() : null)
                .formaPagamentoId(nota.getFormaPagamento() != null ? nota.getFormaPagamento().getId() : null)
                .duplicataId(nota.getDuplicata() != null ? nota.getDuplicata().getId() : null)
                .dsDuplicata(nota.getDuplicata() != null ? nota.getDuplicata().getDescricao() : null)
                .parcelasPrevistas(parcelasPrevistasDTO)
                .build();
        });
    }
    
    @Override
    public Page<NotaFiscalResponseDTO> listarNotasFiscaisWithPaginacao(Pageable pageable) {
        Page<NotaFiscal> page = notaFiscalRepository.findAll(pageable);

        return page.map(nota -> {
            List<ParcelaPrevistaNotaResponseDTO> parcelasPrevistasDTO = nota.getParcelasPrevistas() != null
                ? nota.getParcelasPrevistas().stream()
                    .map(parcela -> ParcelaPrevistaNotaResponseDTO.builder()
                        .id(parcela.getId())
                        .dtVencimentoPrevisto(parcela.getDtVencimentoPrevisto())
                        .valorPrevisto(parcela.getValorPrevisto())
                        .build())
                    .toList()
                : List.of();

            return NotaFiscalResponseDTO.builder()
                .id(nota.getId())
                .numero(nota.getNumero())
                .serie(nota.getSerie())
                .chave(nota.getChave())
                .descricaoObs(nota.getDescricaoObs())
                .valorTotal(nota.getValorTotal())
                .valorDesconto(nota.getValorDesconto())
                .valorIcms(nota.getValorIcms())
                .valorJuros(nota.getValorJuros())
                .valorMulta(nota.getValorMulta())
                .dtCompra(nota.getDtCompra())
                .fornecedorId(nota.getFornecedor() != null ? nota.getFornecedor().getId() : null)
                .fornecedorNome(nota.getFornecedor() != null ? nota.getFornecedor().getNome() : null)
                .tipoNotaId(nota.getTipo() != null ? nota.getTipo().getId() : null)
                .pessoaId(nota.getPessoa() != null ? nota.getPessoa().getId() : null)
                .filialId(nota.getFilial() != null ? nota.getFilial().getId() : null)
                .formaPagamentoId(nota.getFormaPagamento() != null ? nota.getFormaPagamento().getId() : null)
                .duplicataId(nota.getDuplicata() != null ? nota.getDuplicata().getId() : null)
                .dsDuplicata(nota.getDuplicata() != null ? nota.getDuplicata().getDescricao() : null)
                .parcelasPrevistas(parcelasPrevistasDTO)
                .build();
        });
    }
    
    @Override
    @Transactional
    public void salvarNotaFiscal(NotaFiscalRequestDTO dto) {
    	
    	var notaFiscal = notaFiscalRepository.findByNumeroAndSerieAndFornecedorId(dto.getNumero(),dto.getSerie(),dto.getFornecedorId());
    	
    	if(!notaFiscal.isEmpty()) {
    		throw new NegocioException(MensagemEnum.MSGE013.getKey());
    	}
    	

        var fornecedor = fornecedorRepository.findById(dto.getFornecedorId())
                .orElseThrow(() -> new NegocioException(MensagemEnum.MSGE007.getKey()));
        var tipoNota = tipoNotaRepository.findById(dto.getTipoNotaId())
                .orElseThrow(() -> new NegocioException(MensagemEnum.MSGE008.getKey()));
        var filial = filialRepository.findById(dto.getFilialId())
                .orElseThrow(() -> new NegocioException(MensagemEnum.MSGE009.getKey()));
        var idPessoa = UsuarioLogadoUtil.getIdUsuarioLogado();

        NotaFiscal nf = new NotaFiscal();
        nf.setNumero(dto.getNumero());
        nf.setSerie(dto.getSerie());
        nf.setChave(dto.getChave());
        nf.setDescricaoObs(dto.getDescricaoObs());
        nf.setValorTotal(dto.getValorTotal());
        nf.setValorDesconto(dto.getValorDesconto());
        nf.setValorIcms(dto.getValorIcms());
        nf.setValorJuros(dto.getValorJuros());
        nf.setValorMulta(dto.getValorMulta());
        nf.setDtCompra(dto.getDtCompra());
        nf.setDtInclusao(LocalDate.now());
        nf.setFornecedor(fornecedor);
        nf.setTipo(tipoNota);
        nf.setFilial(filial);
        nf.setFormaPagamento(Objects.nonNull(dto.getFormaPagamentoId()) ? formaPagamentoRepository.findById(dto.getFormaPagamentoId()).get() : null);
        if (dto.getDuplicataId() != null) {
            var duplicata = duplicataRepository.findById(dto.getDuplicataId())
                .orElseThrow(() -> new NegocioException(MensagemEnum.MSGE016.getKey()));
            nf.setDuplicata(duplicata);
        }

        notaFiscalRepository.save(nf);
        if (dto.getParcelasPrevistas() != null && !dto.getParcelasPrevistas().isEmpty()) {
            for (var parcelaDto : dto.getParcelasPrevistas()) {
                var parcelaPrevista = ParcelaPrevistaNota.builder()
                        .notaFiscal(nf)
                        .dtVencimentoPrevisto(parcelaDto.getDtVencimentoPrevisto())
                        .valorPrevisto(parcelaDto.getValorPrevisto())
                        .numeroParcela(parcelaDto.getNumeroParcela())
                        .build();
                parcelaPrevistaNotaRepository.save(parcelaPrevista);
            }
        }
    }

    
    @Override
    @Transactional
    public void atualizarNotaFiscal(Long id, NotaFiscalRequestDTO dto) {
    	var notaFiscalSalva = notaFiscalRepository.findByNumeroAndSerieAndFornecedorIdAndIdNot(dto.getNumero(), dto.getSerie(), dto.getFornecedorId(), id);
    	if(!notaFiscalSalva.isEmpty()) {
    		throw new NegocioException(MensagemEnum.MSGE013.getKey());
    	}
    	
        var notaFiscal = notaFiscalRepository.findById(id)
                .orElseThrow(() -> new NegocioException(MensagemEnum.MSGE010.getKey()));

        notaFiscal.setNumero(dto.getNumero());
        notaFiscal.setSerie(dto.getSerie());
        notaFiscal.setChave(dto.getChave());
        notaFiscal.setDescricaoObs(dto.getDescricaoObs());
        notaFiscal.setValorTotal(dto.getValorTotal());
        notaFiscal.setValorDesconto(dto.getValorDesconto());
        notaFiscal.setValorIcms(dto.getValorIcms());
        notaFiscal.setValorJuros(dto.getValorJuros());
        notaFiscal.setValorMulta(dto.getValorMulta());
        notaFiscal.setDtCompra(dto.getDtCompra());
        notaFiscal.setDtAtualizacao(LocalDate.now());

        if (dto.getFornecedorId() != null) {
            var fornecedor = fornecedorRepository.findById(dto.getFornecedorId())
                    .orElseThrow(() -> new NegocioException(MensagemEnum.MSGE007.getKey()));
            notaFiscal.setFornecedor(fornecedor);
        }

        if (dto.getTipoNotaId() != null) {
            var tipo = tipoNotaRepository.findById(dto.getTipoNotaId())
                    .orElseThrow(() -> new NegocioException(MensagemEnum.MSGE008.getKey()));
            notaFiscal.setTipo(tipo);
        }

        if (dto.getPessoaId() != null) {
            var pessoa = pessoaRepository.findById(dto.getPessoaId())
                    .orElseThrow(() -> new NegocioException(MensagemEnum.MSGE009.getKey()));
            notaFiscal.setPessoa(pessoa);
        }
        
        if (dto.getDuplicataId() != null) {
            var duplicata = duplicataRepository.findById(dto.getDuplicataId())
                .orElseThrow(() -> new NegocioException(MensagemEnum.MSGE016.getKey()));
            notaFiscal.setDuplicata(duplicata);
        }
        
        // Se a forma de pagamento for nula, apagar parcelas previstas ---
        if (Objects.isNull(dto.getFormaPagamentoId())) {
            var parcelasExistentes = parcelaPrevistaNotaRepository.findByNotaFiscalId(notaFiscal.getId());
            if (!parcelasExistentes.isEmpty()) {
                parcelaPrevistaNotaRepository.deleteAll(parcelasExistentes);
            }
            notaFiscal.setFormaPagamento(null);
        } else {
            var formaPgto = formaPagamentoRepository.findById(dto.getFormaPagamentoId())
                    .orElseThrow(() -> new NegocioException(MensagemEnum.MSGE011.getKey()));
            notaFiscal.setFormaPagamento(formaPgto);
        }

        var parcelasAtuais = parcelaPrevistaNotaRepository.findByNotaFiscalId(notaFiscal.getId());

        if (dto.getParcelasPrevistas() != null) {
            for (var parcelaDto : dto.getParcelasPrevistas()) {
                if (parcelaDto.getId() != null) {
                    var existente = parcelasAtuais.stream()
                            .filter(p -> p.getId().equals(parcelaDto.getId()))
                            .findFirst()
                            .orElse(null);

                    if (existente != null) {
                        existente.setDtVencimentoPrevisto(parcelaDto.getDtVencimentoPrevisto());
                        existente.setValorPrevisto(parcelaDto.getValorPrevisto());
                        existente.setNumeroParcela(parcelaDto.getNumeroParcela());
                        parcelaPrevistaNotaRepository.save(existente);
                    }
                } else {
                    var nova = ParcelaPrevistaNota.builder()
                            .notaFiscal(notaFiscal)
                            .dtVencimentoPrevisto(parcelaDto.getDtVencimentoPrevisto())
                            .valorPrevisto(parcelaDto.getValorPrevisto())
                            .numeroParcela(parcelaDto.getNumeroParcela())
                            .build();
                    parcelaPrevistaNotaRepository.save(nova);
                }
            }
        }

        var idsEnviados = dto.getParcelasPrevistas() != null
                ? dto.getParcelasPrevistas().stream()
                        .map(p -> p.getId())
                        .filter(idParc -> idParc != null)
                        .toList()
                : List.<Long>of();

        for (var parcela : parcelasAtuais) {
            if (!idsEnviados.contains(parcela.getId())) {
                parcelaPrevistaNotaRepository.delete(parcela);
            }
        }

        notaFiscalRepository.save(notaFiscal);
    }
    
    @Override
    public NotaFiscalResponseDTO buscarNotaFiscalPorId(Long id) {
        var nota = notaFiscalRepository.findById(id)
                .orElseThrow(() -> new NegocioException(MensagemEnum.MSGE010.getKey()));

        List<ParcelaPrevistaNotaResponseDTO> parcelasPrevistasDTO = nota.getParcelasPrevistas() != null
                ? nota.getParcelasPrevistas().stream()
                    .map(parcela -> ParcelaPrevistaNotaResponseDTO.builder()
                            .id(parcela.getId())
                            .dtVencimentoPrevisto(parcela.getDtVencimentoPrevisto())
                            .valorPrevisto(parcela.getValorPrevisto())
                            .build())
                    .toList()
                : List.of();

        return NotaFiscalResponseDTO.builder()
                .id(nota.getId())
                .numero(nota.getNumero())
                .serie(nota.getSerie())
                .chave(nota.getChave())
                .descricaoObs(nota.getDescricaoObs())
                .valorTotal(nota.getValorTotal())
                .valorDesconto(nota.getValorDesconto())
                .valorIcms(nota.getValorIcms())
                .valorJuros(nota.getValorJuros())
                .valorMulta(nota.getValorMulta())
                .dtCompra(nota.getDtCompra())
                .fornecedorId(nota.getFornecedor() != null ? nota.getFornecedor().getId() : null)
                .fornecedorNome(nota.getFornecedor() != null ? nota.getFornecedor().getNome() : null)
                .tipoNotaId(nota.getTipo() != null ? nota.getTipo().getId() : null)
                .pessoaId(nota.getPessoa() != null ? nota.getPessoa().getId() : null)
                .filialId(nota.getFilial() != null ? nota.getFilial().getId() : null)
                .formaPagamentoId(nota.getFormaPagamento() != null ? nota.getFormaPagamento().getId() : null)
                .duplicataId(nota.getDuplicata() != null ? nota.getDuplicata().getId() : null)
                .dsDuplicata(nota.getDuplicata() != null ? nota.getDuplicata().getDescricao() : null)
                .parcelasPrevistas(parcelasPrevistasDTO)
                .build();
    }
    
    @Override
    @Transactional
    public void excluirNotaFiscal(Long id) {
        var notaFiscal = notaFiscalRepository.findById(id)
                .orElseThrow(() -> new NegocioException(MensagemEnum.MSGE010.getKey()));

        notaFiscalRepository.delete(notaFiscal);
    }
    
    @Override
    public List<NotaFiscalResponseDTO> buscarPorNumeroEFornecedor(String numero, Long fornecedorId) {
        List<NotaFiscal> notas = notaFiscalRepository.findByNumeroAndFornecedorId(numero, fornecedorId);

        if (notas.isEmpty()) {
            throw new NegocioException(MensagemEnum.MSGE010.getKey());
        }

        return notas.stream().map(nota -> {
            List<ParcelaPrevistaNotaResponseDTO> parcelasPrevistasDTO = nota.getParcelasPrevistas() != null
                    ? nota.getParcelasPrevistas().stream()
                        .map(parcela -> ParcelaPrevistaNotaResponseDTO.builder()
                                .id(parcela.getId())
                                .dtVencimentoPrevisto(parcela.getDtVencimentoPrevisto())
                                .valorPrevisto(parcela.getValorPrevisto())
                                .build())
                        .toList()
                    : List.of();

            return NotaFiscalResponseDTO.builder()
                    .id(nota.getId())
                    .numero(nota.getNumero())
                    .serie(nota.getSerie())
                    .chave(nota.getChave())
                    .descricaoObs(nota.getDescricaoObs())
                    .valorTotal(nota.getValorTotal())
                    .valorDesconto(nota.getValorDesconto())
                    .valorIcms(nota.getValorIcms())
                    .valorJuros(nota.getValorJuros())
                    .valorMulta(nota.getValorMulta())
                    .dtCompra(nota.getDtCompra())
                    .fornecedorId(nota.getFornecedor() != null ? nota.getFornecedor().getId() : null)
                    .fornecedorNome(nota.getFornecedor() != null ? nota.getFornecedor().getNome() : null)
                    .tipoNotaId(nota.getTipo() != null ? nota.getTipo().getId() : null)
                    .pessoaId(nota.getPessoa() != null ? nota.getPessoa().getId() : null)
                    .filialId(nota.getFilial() != null ? nota.getFilial().getId() : null)
                    .formaPagamentoId(nota.getFormaPagamento() != null ? nota.getFormaPagamento().getId() : null)
                    .parcelasPrevistas(parcelasPrevistasDTO)
                    .build();
        }).toList();
    }
	
}
