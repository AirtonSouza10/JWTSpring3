package com.service.desk.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.desk.dto.TelefoneTipoDTO;
import com.service.desk.repository.TelefoneTipoRepository;
import com.service.desk.service.service.TelefoneService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class TelefoneServiceImpl implements TelefoneService {
	
	@Autowired
	private TelefoneTipoRepository telefoneTipoRepository;

    @Override
    public List<TelefoneTipoDTO> listarTiposTelefone() {
    	var listaTiposTelefone = new ArrayList<TelefoneTipoDTO>();
    	var tipos = telefoneTipoRepository.findAll();
    	tipos.forEach(f->{    		    			
    		var tipo = TelefoneTipoDTO.builder()
    				.id(f.getId())
    				.descricacao(f.getDescricao())
    				.build();
    		
    		listaTiposTelefone.add(tipo);
    	});
    	return listaTiposTelefone;
    }
}
