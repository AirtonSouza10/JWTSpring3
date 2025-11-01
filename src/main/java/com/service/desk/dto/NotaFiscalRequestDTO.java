package com.service.desk.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class NotaFiscalRequestDTO {
    private Long id;

    private String numero;
    private String serie;
    private String chave;
    private String descricaoObs;

    private BigDecimal valorTotal;
    private BigDecimal valorDesconto;
    private BigDecimal valorIcms;
    private BigDecimal valorJuros;
    private BigDecimal valorMulta;

    private LocalDate dtCompra;

    private Long fornecedorId;
    private Long tipoNotaId;
    private Long pessoaId;
    private Long filialId;
    private Long formaPagamentoId;
    private Long duplicataId;
    
    private List<ParcelaPrevistaNotaRequestDTO> parcelasPrevistas;
}
