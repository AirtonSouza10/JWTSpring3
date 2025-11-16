package com.service.desk.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RelatorioCustomizadoResponseDTO {
    private String loja;
    private String fornecedor;
    private String tituloDuplicata;
    private Long prazo;
    private String numeroParcela;
    private BigDecimal valorParcela;
    private String valorParcelaFormatada;
    private LocalDate dtVencimento;
    private String dtVencimentoFormatada;
    private String status; 
}
