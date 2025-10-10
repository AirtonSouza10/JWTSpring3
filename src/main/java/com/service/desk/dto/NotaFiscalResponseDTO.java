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
public class NotaFiscalResponseDTO {
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

    private Date dtCompra;

    private Long fornecedorId;
    private Long tipoNotaId;
    private Long pessoaId;
    private Long filialId;
    
    private List<ParcelaPrevistaNotaResponseDTO> parcelasPrevistas;
}
