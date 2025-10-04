package com.service.desk.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotaDuplicataResponseDTO {
    private Long id;
    private Long duplicataId;
    private BigDecimal valorRateado;
}
