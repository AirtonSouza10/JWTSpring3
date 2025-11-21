package com.service.desk.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class NotaFiscalResumoDTO {
    private Long id;
    private String numero;
    private String serie;
    private String chave;
    private LocalDate dtCompra;
    private BigDecimal valorTotal;
    private Long fornecedorId;
    private String fornecedorNome;
    private Long filialId;
}
