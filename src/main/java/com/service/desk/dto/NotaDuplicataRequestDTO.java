package com.service.desk.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotaDuplicataRequestDTO {
    private Long duplicataId;
    private String descricao;
    private BigDecimal valor;
    private BigDecimal desconto;
    private BigDecimal multa;
    private BigDecimal valorTotal;

    private List<ParcelaRequestDTO> parcelas;
}
