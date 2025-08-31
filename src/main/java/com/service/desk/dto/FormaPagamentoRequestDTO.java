package com.service.desk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FormaPagamentoRequestDTO {
    private Long id;
    private String descricao;
    private Integer qtdeParcelas;
}
