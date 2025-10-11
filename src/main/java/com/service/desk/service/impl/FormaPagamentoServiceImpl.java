package com.service.desk.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.desk.dto.FormaPagamentoRequestDTO;
import com.service.desk.dto.FormaPagamentoResponseDTO;
import com.service.desk.entidade.FormaPagamento;
import com.service.desk.repository.FormaPagamentoRepository;
import com.service.desk.service.service.FormaPagamentoService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class FormaPagamentoServiceImpl implements FormaPagamentoService {
	
	@Autowired
	private FormaPagamentoRepository formaPagamentoRepository;

    @Override
    public List<FormaPagamentoResponseDTO> listarFormasPagamento() {
    	var listaFormasPagamento = new ArrayList<FormaPagamentoResponseDTO>();
    	var formasPagamento = formaPagamentoRepository.findAll();
    	formasPagamento.forEach(f->{    		    			
    		var formaPagamento = FormaPagamentoResponseDTO.builder()
    				.id(f.getId())
    				.descricao(f.getDescricao())
    				.qtdeParcelas(f.getQtdeParcelas())
    				.prazoPrimeiraParcela(f.getPrazoPrimeiraParcela())
    				.intervaloParcelas(f.getIntervaloParcelas())
    				.build();
    		
    		listaFormasPagamento.add(formaPagamento);
    	});
    	return listaFormasPagamento;
    }
    
    @Override
    public FormaPagamentoResponseDTO buscarPorId(Long id) {
        var formaPagamento = formaPagamentoRepository.findById(id).orElseThrow();

        return FormaPagamentoResponseDTO.builder()
                .id(formaPagamento.getId())
                .descricao(formaPagamento.getDescricao())
                .qtdeParcelas(formaPagamento.getQtdeParcelas())
				.prazoPrimeiraParcela(formaPagamento.getPrazoPrimeiraParcela())
				.intervaloParcelas(formaPagamento.getIntervaloParcelas())
                .build();
    }
    
    @Override
    @Transactional
    public void salvarFormaPagamento(FormaPagamentoRequestDTO formaPagamentoRequestDTO) {
        var formaPagamento = FormaPagamento.builder()
                .descricao(formaPagamentoRequestDTO.getDescricao())
                .qtdeParcelas(formaPagamentoRequestDTO.getQtdeParcelas())
				.prazoPrimeiraParcela(formaPagamentoRequestDTO.getPrazoPrimeiraParcela())
				.intervaloParcelas(formaPagamentoRequestDTO.getIntervaloParcelas())
                .build();

        formaPagamentoRepository.save(formaPagamento);	
    }
    
    @Override
    @Transactional
    public void atualizarFormaPagamento(Long id, FormaPagamentoRequestDTO formaPagamentoRequestDTO) {
        var formaPagamento = formaPagamentoRepository.findById(id).orElseThrow();

        formaPagamento.setDescricao(formaPagamentoRequestDTO.getDescricao());
        formaPagamento.setQtdeParcelas(formaPagamentoRequestDTO.getQtdeParcelas());
        formaPagamento.setPrazoPrimeiraParcela(formaPagamentoRequestDTO.getPrazoPrimeiraParcela());
        formaPagamento.setIntervaloParcelas(formaPagamentoRequestDTO.getIntervaloParcelas());

        formaPagamentoRepository.save(formaPagamento);   	
    }
    
    @Override
    @Transactional
    public void deletarFormaPagamento(Long id) {
        var formaPagamento = formaPagamentoRepository.findById(id).orElseThrow();
        formaPagamentoRepository.delete(formaPagamento);
    }
	
}
