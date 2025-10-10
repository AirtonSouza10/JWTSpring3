package com.service.desk.dto;

import java.math.BigDecimal;
import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ParcelaPrevistaNotaRequestDTO {
    private Long id;
    private Date dtVencimentoPrevisto;
    private BigDecimal valorPrevisto;
}
