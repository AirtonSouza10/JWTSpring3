package com.service.desk.service.service;

import java.util.List;

import com.service.desk.dto.TipoRequestDTO;
import com.service.desk.dto.TipoResponseDTO;

public interface TipoService {

	List<TipoResponseDTO> listarTipos();

	void salvarTipo(TipoRequestDTO tipoRequestDTO);

	void atualizarTipo(Long id, TipoRequestDTO tipoRequestDTO);

	void deletarTipo(Long id);

	TipoResponseDTO buscarPorId(Long id);

}
