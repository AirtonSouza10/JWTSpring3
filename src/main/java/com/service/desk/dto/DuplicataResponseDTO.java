package com.service.desk.dto;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DuplicataResponseDTO {
    private Long id;
    private String descricao;
    private BigDecimal valor;
    private BigDecimal desconto;
    private BigDecimal multa;
    private BigDecimal juros;
    private BigDecimal valorTotal;
    private Date dtCriacao;
    private Date dtAtualizacao;
    private Long formaPagamentoId;
    private List<ParcelamentoDTO> parcelas;
}
