package com.service.desk.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.desk.dto.EnderecoTipoDTO;
import com.service.desk.repository.EnderecoTipoRepository;
import com.service.desk.service.service.EnderecoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class EnderecoServiceImpl implements EnderecoService {
	
	@Autowired
	private EnderecoTipoRepository enderecoTipoRepository;

    @Override
    public List<EnderecoTipoDTO> listarTiposEndereco() {
    	var listaTiposEndereco = new ArrayList<EnderecoTipoDTO>();
    	var tipos = enderecoTipoRepository.findAll();
    	tipos.forEach(f->{    		    			
    		var tipo = EnderecoTipoDTO.builder()
    				.id(f.getId())
    				.descricao(f.getDescricao())
    				.build();
    		
    		listaTiposEndereco.add(tipo);
    	});
    	return listaTiposEndereco;
    }
}
