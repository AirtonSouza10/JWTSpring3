package com.service.desk.service.service;

import java.util.List;

import com.service.desk.dto.FornecedorRequestDTO;
import com.service.desk.dto.FornecedorResponseDTO;

public interface FornecedorService {

	List<FornecedorResponseDTO> listarFornecdores();

	void salvarFornecedores(FornecedorRequestDTO fornecedorRequest);

	void atualizarFornecedores(Long id,FornecedorRequestDTO fornecedorRequest);

	void atualizarStatus(Long id, Boolean ativo);

	FornecedorResponseDTO buscarPorId(Long id);
}
