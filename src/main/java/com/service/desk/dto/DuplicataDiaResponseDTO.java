package com.service.desk.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DuplicataDiaResponseDTO {
    private Long id;
    private String descricao;
    private BigDecimal valor;
    private String situacao;
    private LocalDate dtVencimento;
}
