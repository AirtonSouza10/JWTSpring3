package com.service.desk.service.service;

import java.util.List;

import com.service.desk.dto.StatusContaRequestDTO;
import com.service.desk.dto.StatusContaResponseDTO;

public interface StatusContaService {

	List<StatusContaResponseDTO> listarSituacoesConta();

	void salvarStatus(StatusContaRequestDTO statusRequestDTO);

	void atualizarStatus(Long id, StatusContaRequestDTO formaPagamentoResponseDTO);

	StatusContaResponseDTO buscarPorId(Long id);

	void deletarStatus(Long id);


}
