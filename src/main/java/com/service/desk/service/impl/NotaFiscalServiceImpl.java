package com.service.desk.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.desk.dto.NotaDuplicataRequestDTO;
import com.service.desk.dto.NotaDuplicataResponseDTO;
import com.service.desk.dto.NotaFiscalRequestDTO;
import com.service.desk.dto.NotaFiscalResponseDTO;
import com.service.desk.dto.ParcelaRequestDTO;
import com.service.desk.entidade.Duplicata;
import com.service.desk.entidade.NotaDuplicata;
import com.service.desk.entidade.NotaFiscal;
import com.service.desk.entidade.Parcela;
import com.service.desk.repository.DuplicataRepository;
import com.service.desk.repository.FilialRepository;
import com.service.desk.repository.FornecedorRepository;
import com.service.desk.repository.NotaDuplicataRepository;
import com.service.desk.repository.NotaFiscalRepository;
import com.service.desk.repository.ParcelaRepository;
import com.service.desk.repository.PessoaRepository;
import com.service.desk.repository.TipoNotaRepository;
import com.service.desk.service.service.NotaFiscalService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
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
    private NotaDuplicataRepository notaDuplicataRepository;
	@Autowired
    private DuplicataRepository duplicataRepository;
	@Autowired
    private ParcelaRepository parcelaRepository;

    @Override
    public List<NotaFiscalResponseDTO> listarNotasFiscais() {
        var notasFiscais = notaFiscalRepository.findAll();

        List<NotaFiscalResponseDTO> notasFiscaisList = notasFiscais.stream().map(f -> {

            List<NotaDuplicataResponseDTO> duplicatasDTO = f.getNotasDuplicata().stream().map(nd ->
                NotaDuplicataResponseDTO.builder()
                    .id(nd.getId())
                    .duplicataId(nd.getDuplicata().getId())
                    .valorRateado(nd.getValorRateado())
                    .build()
            ).toList();

            return NotaFiscalResponseDTO.builder()
                .id(f.getId())
                .numero(f.getNumero())
                .serie(f.getSerie())
                .chave(f.getChave())
                .descricaoObs(f.getDescricaoObs())
                .valorTotal(f.getValorTotal())
                .valorDesconto(f.getValorDesconto())
                .valorIcms(f.getValorIcms())
                .valorJuros(f.getValorJuros())
                .valorMulta(f.getValorMulta())
                .dtCompra(f.getDtCompra())
                .fornecedorId(f.getFornecedor() != null ? f.getFornecedor().getId() : null)
                .tipoNotaId(f.getTipo() != null ? f.getTipo().getId() : null)
                .pessoaId(f.getPessoa() != null ? f.getPessoa().getId() : null)
                .duplicatas(duplicatasDTO)
                .build();

        }).toList();

        return notasFiscaisList;
    }
    
    @Override
    @Transactional
    public void salvarNotaFiscal(NotaFiscalRequestDTO dto) {

        var fornecedor = fornecedorRepository.findById(dto.getFornecedorId())
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));
        var tipoNota = tipoNotaRepository.findById(dto.getTipoNotaId())
                .orElseThrow(() -> new RuntimeException("Tipo de nota não encontrado"));
        var pessoa = pessoaRepository.findById(dto.getPessoaId())
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));
        var filial = filialRepository.findById(dto.getFilialId())
                .orElseThrow(() -> new RuntimeException("Filial não encontrada"));

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
        nf.setFornecedor(fornecedor);
        nf.setTipo(tipoNota);
        nf.setPessoa(pessoa);
        nf.setFilial(filial);

        notaFiscalRepository.save(nf);

        if (dto.getDuplicatas() != null) {
            for (NotaDuplicataRequestDTO dupDto : dto.getDuplicatas()) {

                Duplicata duplicata;
                if (dupDto.getDuplicataId() != null) {
                    duplicata = duplicataRepository.findById(dupDto.getDuplicataId())
                            .orElseThrow(() -> new RuntimeException("Duplicata não encontrada"));
                } else {
                    duplicata = new Duplicata();
                    duplicata.setDescricao(dupDto.getDescricao());
                    duplicata.setValor(dupDto.getValor());
                    duplicata.setDesconto(dupDto.getDesconto());
                    duplicata.setMulta(dupDto.getMulta());
                    duplicata.setValorTotal(dupDto.getValorTotal());
                    duplicata.setDtCriacao(new java.util.Date());
                    duplicataRepository.save(duplicata);
                }

                NotaDuplicata notaDuplicata = new NotaDuplicata();
                notaDuplicata.setNotaFiscal(nf);
                notaDuplicata.setDuplicata(duplicata);
                notaDuplicataRepository.save(notaDuplicata);

                if (dupDto.getParcelas() != null) {
                    for (ParcelaRequestDTO parcDto : dupDto.getParcelas()) {
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
        }
    }

    
    @Override
    @Transactional
    public void atualizarNotaFiscal(Long id, NotaFiscalRequestDTO dto) {
        var notaFiscal = notaFiscalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nota Fiscal não encontrada para id " + id));

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

        if (dto.getFornecedorId() != null) {
            var fornecedor = fornecedorRepository.findById(dto.getFornecedorId())
                    .orElseThrow(() -> new EntityNotFoundException("Fornecedor não encontrado"));
            notaFiscal.setFornecedor(fornecedor);
        }
        if (dto.getTipoNotaId() != null) {
            var tipo = tipoNotaRepository.findById(dto.getTipoNotaId())
                    .orElseThrow(() -> new EntityNotFoundException("Tipo de Nota não encontrado"));
            notaFiscal.setTipo(tipo);
        }
        if (dto.getPessoaId() != null) {
            var pessoa = pessoaRepository.findById(dto.getPessoaId())
                    .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));
            notaFiscal.setPessoa(pessoa);
        }
        notaFiscalRepository.save(notaFiscal); 	
    }
	
}
