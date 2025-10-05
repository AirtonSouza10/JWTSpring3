package com.service.desk.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.desk.dto.FilialRequestDTO;
import com.service.desk.dto.FilialResponseDTO;
import com.service.desk.entidade.Filial;
import com.service.desk.repository.FilialRepository;
import com.service.desk.service.service.FilialService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class FilialServiceImpl implements FilialService {
	
	@Autowired
	private FilialRepository filialRepository;

    @Override
    public List<FilialResponseDTO> listarFiliais() {
    	var listaFiliais = new ArrayList<FilialResponseDTO>();
    	var filiais = filialRepository.findAll();
    	filiais.forEach(f->{    		    			
    		var filial = FilialResponseDTO.builder()
    				.id(f.getId())
    				.nome(f.getNome())
    				.identificacao(f.getIdentificacao())
    				.email(f.getEmail())
    				.tpidentificacao(f.getTpIdentificacao())
    				.ativo(f.getAtivo())
    				.build();
    		
    		listaFiliais.add(filial);
    	});
    	return listaFiliais;
    }
   
    @Override
    public FilialResponseDTO buscarPorId(Long id) {
        var filial = filialRepository.findById(id).orElseThrow();
        
        return FilialResponseDTO.builder()
                .id(filial.getId())
                .nome(filial.getNome())
                .identificacao(filial.getIdentificacao())
                .tpidentificacao(filial.getTpIdentificacao())
                .email(filial.getEmail())
                .ativo(filial.getAtivo())
                .build();
    }
    
    @Override
    @Transactional
    public void salvarFilial(FilialRequestDTO dto) {
        var filial = Filial.builder()
        		.identificacao(dto.getIdentificacao())
        		.tpIdentificacao(dto.getIdentificacao().length() == 11 ? 4 : 3)
        		.email(dto.getEmail())
        		.nome(dto.getNome())
        		.ativo(true)
                .build();

        filialRepository.save(filial);	
    }
    
    @Override
    @Transactional
    public void atualizarFilial(Long id, FilialRequestDTO dto) {
        var filial = filialRepository.findById(id).orElseThrow();
        
        filial.setNome(dto.getNome());
        filial.setIdentificacao(dto.getIdentificacao());
        filial.setTpIdentificacao(dto.getIdentificacao().length() == 11 ? 4 : 3);
        filial.setEmail(dto.getEmail());

        filialRepository.save(filial);   	
    }
    
    @Override
    @Transactional
    public void atualizarStatus(Long id, Boolean ativo) {
        var filial = filialRepository.findById(id).orElseThrow();
        filial.setAtivo(ativo);
        filialRepository.save(filial);
    }
}
