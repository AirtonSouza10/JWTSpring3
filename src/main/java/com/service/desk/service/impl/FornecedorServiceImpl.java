package com.service.desk.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.desk.dto.EnderecoDTO;
import com.service.desk.dto.EnderecoTipoDTO;
import com.service.desk.dto.FornecedorRequestDTO;
import com.service.desk.dto.FornecedorResponseDTO;
import com.service.desk.dto.TelefoneDTO;
import com.service.desk.dto.TelefoneTipoDTO;
import com.service.desk.entidade.Endereco;
import com.service.desk.entidade.Fornecedor;
import com.service.desk.entidade.Telefone;
import com.service.desk.repository.EnderecoTipoRepository;
import com.service.desk.repository.FornecedorRepository;
import com.service.desk.repository.PessoaRepository;
import com.service.desk.repository.TelefoneTipoRepository;
import com.service.desk.service.service.FornecedorService;
import com.service.desk.utils.UsuarioLogadoUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class FornecedorServiceImpl implements FornecedorService {
	@Autowired
	private FornecedorRepository fornecedorRepository;

	@Autowired
	private TelefoneTipoRepository telefoneTipoRepository;

	@Autowired
	private EnderecoTipoRepository enderecoTipoRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	    
    @Override
    public List<FornecedorResponseDTO> listarFornecdores() {
    	var listaFornecedores = new ArrayList<FornecedorResponseDTO>();
    	var fornecedores = fornecedorRepository.findAll();
    	fornecedores.forEach(f->{
    		var telefonesList = f.getTelefones().stream().map(t-> TelefoneDTO.builder()
    				.id(t.getId())
    				.numero(t.getNumero())
    				.tpTelefone(TelefoneTipoDTO.builder().id(t.getTpTelefone().getId()).descricao(t.getTpTelefone().getDescricao()).build())
    				.build()
    			).toList();
    		
    		var enderecosList = f.getEnderecos().stream().map(e-> EnderecoDTO.builder()
    				.id(e.getId())
    				.logradouro(e.getLogradouro())
    				.complemento(e.getComplemento())
    				.numero(e.getNumero())
    				.cidade(e.getCidade())
    				.estado(e.getEstado())
    				.bairro(e.getBairro())
    				.uf(e.getUf())
    				.cep(e.getCep())
    				.tipoEndereco(EnderecoTipoDTO.builder().id(e.getEnderecoTipo().getId()).descricao(e.getEnderecoTipo().getDescricao()).build())
    				.build()
    				).toList();
    			
    		var fornecedor = FornecedorResponseDTO.builder()
    				.id(f.getId())
    				.identificacao(f.getIdentificacao())
    				.tpIdentificacao(f.getTpIdentificacao())
    				.telefones(telefonesList)
    				.enderecos(enderecosList)
    				.nome(f.getNome())
    				.email(f.getEmail())
    				.ativo(f.getAtivo())
    				.build();
    		
    		listaFornecedores.add(fornecedor);
    	});
    	return listaFornecedores;
    }
    
    @Override
    @Transactional
    public void salvarFornecedores(FornecedorRequestDTO fornecedorRequest) {
        var fornecedor = Fornecedor.builder()
                .identificacao(fornecedorRequest.getIdentificacao())
                .nome(fornecedorRequest.getNome())
                .tpIdentificacao(fornecedorRequest.getIdentificacao().length() ==11 ? 4 : 3)
                .email(fornecedorRequest.getEmail())
                .ativo(true)
                .build();

        var telefones = fornecedorRequest.getTelefones().stream().map(telDto -> {
            var telefone = new Telefone();
            telefone.setId(telDto.getId());
            telefone.setNumero(telDto.getNumero());
            var tipo = telefoneTipoRepository.findById(telDto.getTpTelefone().getId()).orElseThrow();
            telefone.setTpTelefone(tipo);
            telefone.setFornecedor(fornecedor);
            return telefone;
        }).toList();

        var enderecos = fornecedorRequest.getEnderecos().stream().map(endDto -> {
            var endereco = new Endereco();
            endereco.setLogradouro(endDto.getLogradouro());
            endereco.setNumero(endDto.getNumero());
            endereco.setComplemento(endDto.getComplemento());
            endereco.setBairro(endDto.getBairro());
            endereco.setCidade(endDto.getCidade());
            endereco.setEstado(endDto.getEstado());
            endereco.setUf(endDto.getUf());
            endereco.setCep(endDto.getCep());

            var tipo = enderecoTipoRepository.findById(endDto.getTipoEndereco().getId()).orElseThrow();

            endereco.setEnderecoTipo(tipo);
            endereco.setFornecedor(fornecedor);
            return endereco;
        }).toList();

        fornecedor.setTelefones(telefones);
        fornecedor.setEnderecos(enderecos);
        
        fornecedorRepository.save(fornecedor);	
    }
    
    @Override
    @Transactional
    public void atualizarFornecedores(Long id, FornecedorRequestDTO fornecedorRequest) {
        Fornecedor fornecedor = fornecedorRepository.findById(id).orElseThrow();

        fornecedor.setNome(fornecedorRequest.getNome());
        fornecedor.setIdentificacao(fornecedorRequest.getIdentificacao());
        fornecedor.setTpIdentificacao(fornecedorRequest.getIdentificacao().length() == 11 ? 4 : 3);
        fornecedor.setEmail(fornecedorRequest.getEmail());
        fornecedor.setAtivo(fornecedorRequest.getAtivo());

        // Certifique-se de que as listas são mutáveis
        if (fornecedor.getTelefones() == null) {
            fornecedor.setTelefones(new ArrayList<>());
        }
        if (fornecedor.getEnderecos() == null) {
            fornecedor.setEnderecos(new ArrayList<>());
        }

        // Limpar listas existentes (mutáveis)
        fornecedor.getTelefones().clear();
        fornecedor.getEnderecos().clear();

        // Preencher telefones
        fornecedorRequest.getTelefones().forEach(telDto -> {
            Telefone telefone = new Telefone();
            telefone.setId(telDto.getId()); // null se novo
            telefone.setNumero(telDto.getNumero());
            telefone.setFornecedor(fornecedor);

            var tipo = telefoneTipoRepository.findById(telDto.getTpTelefone().getId())
                    .orElseThrow();
            telefone.setTpTelefone(tipo);

            fornecedor.getTelefones().add(telefone);
        });

        // Preencher endereços
        fornecedorRequest.getEnderecos().forEach(endDto -> {
            Endereco endereco = new Endereco();
            endereco.setId(endDto.getId()); // null se novo
            endereco.setLogradouro(endDto.getLogradouro());
            endereco.setNumero(endDto.getNumero());
            endereco.setComplemento(endDto.getComplemento());
            endereco.setBairro(endDto.getBairro());
            endereco.setCidade(endDto.getCidade());
            endereco.setEstado(endDto.getEstado());
            endereco.setUf(endDto.getUf());
            endereco.setCep(endDto.getCep());
            endereco.setFornecedor(fornecedor);

            var tipo = enderecoTipoRepository.findById(endDto.getTipoEndereco().getId())
                    .orElseThrow();
            endereco.setEnderecoTipo(tipo);

            fornecedor.getEnderecos().add(endereco);
        });

        fornecedorRepository.save(fornecedor);
    }


    
    @Override
    public FornecedorResponseDTO buscarPorId(Long id) {
        var fornecedor = fornecedorRepository.findById(id).orElseThrow();

        var telefonesList = fornecedor.getTelefones().stream().map(t -> TelefoneDTO.builder()
                .id(t.getId())
                .numero(t.getNumero())
                .tpTelefone(TelefoneTipoDTO.builder().id(t.getTpTelefone().getId()).descricao(t.getTpTelefone().getDescricao()).build())
                .build()).toList();

        var enderecosList = fornecedor.getEnderecos().stream().map(e -> EnderecoDTO.builder()
                .id(e.getId())
                .logradouro(e.getLogradouro())
                .complemento(e.getComplemento())
                .numero(e.getNumero())
                .cidade(e.getCidade())
                .estado(e.getEstado())
                .bairro(e.getBairro())
                .uf(e.getUf())
                .cep(e.getCep())
                .tipoEndereco(EnderecoTipoDTO.builder().id(e.getEnderecoTipo().getId()).descricao(e.getEnderecoTipo().getDescricao()).build())
                .build()).toList();

        return FornecedorResponseDTO.builder()
                .id(fornecedor.getId())
                .nome(fornecedor.getNome())
                .identificacao(fornecedor.getIdentificacao())
                .tpIdentificacao(fornecedor.getTpIdentificacao())
                .telefones(telefonesList)
                .enderecos(enderecosList)
                .ativo(fornecedor.getAtivo())
                .email(fornecedor.getEmail())
                .build();
    }

    @Override
    @Transactional
    public void atualizarStatus(Long id, Boolean ativo) {
        var fornecedor = fornecedorRepository.findById(id).orElseThrow();
        fornecedor.setAtivo(ativo);
        fornecedorRepository.save(fornecedor);
    }

}
