package com.service.desk.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.desk.dto.TipoNotaRequestDTO;
import com.service.desk.dto.TipoNotaResponseDTO;
import com.service.desk.entidade.TipoNota;
import com.service.desk.repository.TipoNotaRepository;
import com.service.desk.service.service.TipoNotaService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TipoNotaServiceImpl implements TipoNotaService {
	
	@Autowired
	private TipoNotaRepository tipoNotaRepository;

    @Override
    public List<TipoNotaResponseDTO> listarTiposNota() {
    	var listaTiposNota = new ArrayList<TipoNotaResponseDTO>();
    	var tiposNota = tipoNotaRepository.findAll();
    	tiposNota.forEach(f->{    		    			
    		var tipoNota = TipoNotaResponseDTO.builder()
    				.id(f.getId())
    				.descricao(f.getDescricao())
    				.build();
    		
    		listaTiposNota.add(tipoNota);
    	});
    	return listaTiposNota;
    }
    
    @Override
    @Transactional
    public void salvarTipoNota(TipoNotaRequestDTO tipoNotaRequestDTO) {
        var tipoNota = TipoNota.builder()
                .descricao(tipoNotaRequestDTO.getDescricao())
                .build();

        tipoNotaRepository.save(tipoNota);	
    }
    
    @Override
    @Transactional
    public void atualizarTipoNota(Long id, TipoNotaRequestDTO tipoNotaRequestDTO) {
        var tipoNota = tipoNotaRepository.findById(id).orElseThrow();

        tipoNota.setDescricao(tipoNotaRequestDTO.getDescricao());

        tipoNotaRepository.save(tipoNota);   	
    }
	
    @Override
    @Transactional
    public void deletarTipoNota(Long id) {
        var tipoNota = tipoNotaRepository.findById(id).orElseThrow();
        tipoNotaRepository.delete(tipoNota);
    }
    
    @Override
    public TipoNotaResponseDTO buscarPorId(Long id) {
        var tipoNota = tipoNotaRepository.findById(id).orElseThrow();

        return TipoNotaResponseDTO.builder()
                .id(tipoNota.getId())
                .descricao(tipoNota.getDescricao())
                .build();
    }	
}
