package com.service.desk.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ParcelaBuscaGeralDTO {

    private Long parcelaId;
    private String numeroParcela;
    private BigDecimal valorTotal;
    private LocalDate dtVencimento;
    private LocalDate dtPagamento;
    private String status;

    private Long duplicataId;
    private String descricaoDuplicata;

    private Long fornecedorId;
    private String fornecedorNome;
}
