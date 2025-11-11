package com.service.desk.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParcelaResponseDTO {
    private BigDecimal valor;
    private BigDecimal desconto;
    private BigDecimal multa;
    private BigDecimal valorTotal;
    private Date dtVencimento;
    private String observacao;
    private Long tipoPagamentoId;
    private String dsTipoPagamento;
}
