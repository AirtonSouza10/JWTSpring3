package com.service.desk.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.desk.dto.TipoRequestDTO;
import com.service.desk.dto.TipoResponseDTO;
import com.service.desk.entidade.Tipo;
import com.service.desk.repository.TipoRepository;
import com.service.desk.service.service.TipoService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TipoServiceImpl implements TipoService {
	
	@Autowired
	private TipoRepository tipoRepository;

    @Override
    public List<TipoResponseDTO> listarTipos() {
    	var listaTipos = new ArrayList<TipoResponseDTO>();
    	var tipos = tipoRepository.findAll();
    	tipos.forEach(f->{    		    			
    		var tipo = TipoResponseDTO.builder()
    				.id(f.getId())
    				.descricao(f.getDescricao())
    				.build();
    		
    		listaTipos.add(tipo);
    	});
    	return listaTipos;
    }
    
    @Override
    @Transactional
    public void salvarTipo(TipoRequestDTO tipoRequestDTO) {
        var tipo = Tipo.builder()
                .descricao(tipoRequestDTO.getDescricao())
                .build();

        tipoRepository.save(tipo);	
    }
    
    @Override
    @Transactional
    public void atualizarTipo(Long id, TipoRequestDTO tipoRequestDTO) {
        var tipo = tipoRepository.findById(id).orElseThrow();

        tipo.setDescricao(tipoRequestDTO.getDescricao());

        tipoRepository.save(tipo);   	
    }
	
    @Override
    @Transactional
    public void deletarTipo(Long id) {
        var tipo = tipoRepository.findById(id).orElseThrow();
        tipoRepository.delete(tipo);
    }
    
    @Override
    public TipoResponseDTO buscarPorId(Long id) {
        var tipo = tipoRepository.findById(id).orElseThrow();

        return TipoResponseDTO.builder()
                .id(tipo.getId())
                .descricao(tipo.getDescricao())
                .build();
    }	
}
