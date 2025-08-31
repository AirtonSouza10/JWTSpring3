package com.service.desk.service.service;

import java.util.List;

import com.service.desk.dto.TipoPagamentoRequestDTO;
import com.service.desk.dto.TipoPagamentoResponseDTO;

public interface TipoPagamentoService {

	List<TipoPagamentoResponseDTO> listarTiposPagamento();

	void salvarTipoPagamento(TipoPagamentoRequestDTO tipoPagamentoRequestDTO);

	void atualizarTipoPagamento(Long id, TipoPagamentoRequestDTO tipoPagamentoRequestDTO);

}
