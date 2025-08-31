package com.service.desk.service.service;

import java.util.List;

import com.service.desk.dto.TipoNotaRequestDTO;
import com.service.desk.dto.TipoNotaResponseDTO;

public interface TipoNotaService {

	List<TipoNotaResponseDTO> listarTiposNota();

	void salvarTipoNota(TipoNotaRequestDTO tipoNotaRequestDTO);

	void atualizarTipoNota(Long id, TipoNotaRequestDTO tipoNotaRequestDTO);


}
