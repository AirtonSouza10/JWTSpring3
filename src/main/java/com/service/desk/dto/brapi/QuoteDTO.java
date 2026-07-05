package com.service.desk.dto.brapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuoteDTO {
    private BigDecimal lastPrice;

    private BigDecimal changePercent;

    private Long volume;
    private BigDecimal marketCap;
}
