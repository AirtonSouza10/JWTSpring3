package com.service.desk.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ParcelaPrevistaResponseDTO {
    private String numeroNota;
    private BigDecimal valor;
    private LocalDate dtVencimento;
}
