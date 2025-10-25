package com.service.desk.service.service;

import java.util.List;

import com.service.desk.dto.NotaFiscalRequestDTO;
import com.service.desk.dto.NotaFiscalResponseDTO;

public interface NotaFiscalService {

	List<NotaFiscalResponseDTO> listarNotasFiscais();

	void salvarNotaFiscal(NotaFiscalRequestDTO dto);

	void atualizarNotaFiscal(Long id, NotaFiscalRequestDTO dto);

	NotaFiscalResponseDTO buscarNotaFiscalPorId(Long id);

	void excluirNotaFiscal(Long id);

	List<NotaFiscalResponseDTO> buscarPorNumeroEFornecedor(String numero, Long fornecedorId);
}
