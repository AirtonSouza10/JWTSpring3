package com.service.desk.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.desk.dto.StatusContaRequestDTO;
import com.service.desk.dto.StatusContaResponseDTO;
import com.service.desk.entidade.StatusConta;
import com.service.desk.repository.StatusContaRepository;
import com.service.desk.service.service.StatusContaService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class StatusContaServiceImpl implements StatusContaService {
	
	@Autowired
	private StatusContaRepository statusContaRepository;

    @Override
    public List<StatusContaResponseDTO> listarSituacoesConta() {
    	var listaStatus = new ArrayList<StatusContaResponseDTO>();
    	var statusConta = statusContaRepository.findAll();
    	statusConta.forEach(f->{    		    			
    		var status = StatusContaResponseDTO.builder()
    				.id(f.getId())
    				.descricao(f.getDescricao())
    				.build();
    		
    		listaStatus.add(status);
    	});
    	return listaStatus;
    }
    
    @Override
    @Transactional
    public void salvarStatus(StatusContaRequestDTO statusRequestDTO) {
        var status = StatusConta.builder()
                .descricao(statusRequestDTO.getDescricao())
                .build();

        statusContaRepository.save(status);	
    }
    
    @Override
    @Transactional
    public void atualizarStatus(Long id, StatusContaRequestDTO formaPagamentoResponseDTO) {
        var status = statusContaRepository.findById(id).orElseThrow();

        status.setDescricao(formaPagamentoResponseDTO.getDescricao());

        statusContaRepository.save(status);   	
    }
	
}
