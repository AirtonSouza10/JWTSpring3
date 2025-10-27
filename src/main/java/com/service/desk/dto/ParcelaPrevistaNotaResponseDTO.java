package com.service.desk.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ParcelaPrevistaNotaResponseDTO {
    private Long id;
    private LocalDate dtVencimentoPrevisto;
    private BigDecimal valorPrevisto;
}
