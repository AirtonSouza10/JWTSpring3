package com.service.desk.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParcelaResumoDTO {
    private Long id;
    private String numeroParcela;
    private BigDecimal valorTotal;
    private LocalDate dtVencimento;
    private LocalDate dtPagamento;
    private String status;
}
