package com.service.desk.service.service;

import java.util.List;

import com.service.desk.dto.FilialRequestDTO;
import com.service.desk.dto.FilialResponseDTO;

public interface FilialService {

	List<FilialResponseDTO> listarFiliais();

	void salvarFilial(FilialRequestDTO filialRequestDTO);

	void atualizarFilial(Long id, FilialRequestDTO dto);

	FilialResponseDTO buscarPorId(Long id);

	void atualizarStatus(Long id, Boolean ativo);


}
