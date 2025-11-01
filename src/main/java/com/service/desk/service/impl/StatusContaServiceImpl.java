package com.service.desk.service.impl;

import java.time.LocalDate;
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
                .dtInclusao(LocalDate.now())
                .build();

        statusContaRepository.save(status);	
    }
    
    @Override
    @Transactional
    public void atualizarStatus(Long id, StatusContaRequestDTO statusRequestDTO) {
        var status = statusContaRepository.findById(id).orElseThrow();

        status.setDescricao(statusRequestDTO.getDescricao());
        status.setDtAtualizacao(LocalDate.now());

        statusContaRepository.save(status);   	
    }
    
    @Override
    public StatusContaResponseDTO buscarPorId(Long id) {
        var status = statusContaRepository.findById(id).orElseThrow();

        return StatusContaResponseDTO.builder()
                .id(status.getId())
                .descricao(status.getDescricao())
                .build();
    }

    @Override
    @Transactional
    public void deletarStatus(Long id) {
        var status = statusContaRepository.findById(id).orElseThrow();

        statusContaRepository.delete(status);
    }
	
}
