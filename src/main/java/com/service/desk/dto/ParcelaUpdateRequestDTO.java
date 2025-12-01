package com.service.desk.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ParcelaUpdateRequestDTO {

    private Long statusId;

    private Long tipoPagamentoId;

    private String numeroParcela;

    private BigDecimal valorTotal;

    private LocalDate dtVencimento;

    private LocalDate dtPagamento;

    private String observacao;

    private BigDecimal valorPago;
}
