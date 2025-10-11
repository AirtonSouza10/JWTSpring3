package com.service.desk.dto;

import java.math.BigDecimal;
import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ParcelamentoDTO {
    private Long id;
    private BigDecimal valorTotal;
    private Date dtVencimento;
    private Date dtCriacao;
    private Date dtAtualizacao;
    private Long duplicataId;
}
