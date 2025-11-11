package com.service.desk.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BaixaParcelaRequestDTO {
    private Long id;
    private LocalDate dtPagamento;
    private BigDecimal valorPago;
    private String Observacao;
    private Long tipoPagamentoId;
}
