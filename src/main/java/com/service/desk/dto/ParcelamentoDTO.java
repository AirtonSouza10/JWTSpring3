package com.service.desk.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ParcelamentoDTO {
    private Long id;
    private String numeroParcela;
    private BigDecimal valorTotal;
    private LocalDate dtVencimento;
    private LocalDate dtCriacao;
    private LocalDate dtAtualizacao;
    private Long duplicataId;
}
