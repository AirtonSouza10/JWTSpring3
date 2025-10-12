package com.service.desk.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.service.desk.dto.EnderecoDTO;
import com.service.desk.dto.EnderecoTipoDTO;
import com.service.desk.repository.EnderecoTipoRepository;
import com.service.desk.service.service.EnderecoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class EnderecoServiceImpl implements EnderecoService {
    private static final String VIACEP_URL = "https://viacep.com.br/ws/{cep}/json/";

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
  
    @Override
    public EnderecoDTO buscarPorCep(String cep) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                    VIACEP_URL,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<String, Object>>() {},
                    cep
            );

            Map<String, Object> response = responseEntity.getBody();

            if (response == null || response.containsKey("erro")) {
                throw new RuntimeException("CEP não encontrado ou inválido.");
            }

            EnderecoDTO endereco = new EnderecoDTO();
            endereco.setCep((String) response.get("cep"));
            endereco.setLogradouro((String) response.get("logradouro"));
            endereco.setComplemento((String) response.get("complemento"));
            endereco.setBairro((String) response.get("bairro"));
            endereco.setCidade((String) response.get("localidade"));
            endereco.setUf((String) response.get("uf"));	
            endereco.setEstado((String) response.get("estado"));

            return endereco;
        } catch (Exception e) {
            log.error("Erro ao buscar CEP {}: {}", cep, e.getMessage());
            throw new RuntimeException("Falha ao consultar CEP: " + cep);
        }
    }
}
