package com.service.desk.service.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.service.desk.dto.DuplicataRequestDTO;
import com.service.desk.dto.DuplicataResponseDTO;

public interface DuplicataService {

	List<DuplicataResponseDTO> listarDuplicatas();

	DuplicataResponseDTO buscarDuplicataPorId(Long id);

	void salvarDuplicata(DuplicataRequestDTO dto);

	void atualizarDuplicata(Long id, DuplicataRequestDTO dto);

	void excluirDuplicata(Long id);

	Page<DuplicataResponseDTO> listarDuplicatasPaginadas(int pagina, int tamanho);

	Page<DuplicataResponseDTO> buscarDuplicatasPorNumeroPaginadas(String numero, int pagina, int tamanho);

}
