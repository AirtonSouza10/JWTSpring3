package com.service.desk.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.desk.dto.TipoPagamentoRequestDTO;
import com.service.desk.dto.TipoPagamentoResponseDTO;
import com.service.desk.entidade.TipoPagamento;
import com.service.desk.repository.TipoPagamentoRepository;
import com.service.desk.service.service.TipoPagamentoService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class TipoPagamentoServiceImpl implements TipoPagamentoService {
	
	@Autowired
	private TipoPagamentoRepository tipoPagamentoRepository;

    @Override
    public List<TipoPagamentoResponseDTO> listarTiposPagamento() {
    	var listaTiposPagamento = new ArrayList<TipoPagamentoResponseDTO>();
    	var tiposPagamento = tipoPagamentoRepository.findAll();
    	tiposPagamento.forEach(f->{    		    			
    		var tipoPagamento = TipoPagamentoResponseDTO.builder()
    				.id(f.getId())
    				.descricao(f.getDescricao())
    				.build();
    		
    		listaTiposPagamento.add(tipoPagamento);
    	});
    	return listaTiposPagamento;
    }
    
    @Override
    public TipoPagamentoResponseDTO buscarPorId(Long id) {
        var tipoPagamento = tipoPagamentoRepository.findById(id).orElseThrow();
        return TipoPagamentoResponseDTO.builder()
                .id(tipoPagamento.getId())
                .descricao(tipoPagamento.getDescricao())
                .build();
    }
    
    @Override
    @Transactional
    public void salvarTipoPagamento(TipoPagamentoRequestDTO tipoPagamentoRequestDTO) {
        var tipoPagamento = TipoPagamento.builder()
                .descricao(tipoPagamentoRequestDTO.getDescricao())
                .dtInclusao(LocalDate.now())
                .build();

        tipoPagamentoRepository.save(tipoPagamento);	
    }
    
    @Override
    @Transactional
    public void atualizarTipoPagamento(Long id, TipoPagamentoRequestDTO tipoPagamentoRequestDTO) {
        var formaPagamento = tipoPagamentoRepository.findById(id).orElseThrow();

        formaPagamento.setDescricao(tipoPagamentoRequestDTO.getDescricao());
        formaPagamento.setDtAtualizacao(LocalDate.now());

        tipoPagamentoRepository.save(formaPagamento);   	
    }
    
    @Override
    @Transactional
    public void deletarTipoPagamento(Long id) {
        var tipoPagamento = tipoPagamentoRepository.findById(id).orElseThrow();
        tipoPagamentoRepository.delete(tipoPagamento);
    }
	
	
}
