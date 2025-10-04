package com.service.desk.service.service;

import java.util.List;

import com.service.desk.dto.FormaPagamentoRequestDTO;
import com.service.desk.dto.FormaPagamentoResponseDTO;

public interface FormaPagamentoService {

	List<FormaPagamentoResponseDTO> listarFormasPagamento();

	void salvarFormaPagamento(FormaPagamentoRequestDTO formaPagamentoRequestDTO);

	void atualizarFormaPagamento(Long id, FormaPagamentoRequestDTO formaPagamentoRequestDTO);

	FormaPagamentoResponseDTO buscarPorId(Long id);

	void deletarFormaPagamento(Long id);

}
