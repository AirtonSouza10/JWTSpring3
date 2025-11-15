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
public class DuplicataResponseDTO {
    private Long id;
    private String descricao;
    private BigDecimal valor;
    private BigDecimal desconto;
    private BigDecimal multa;
    private BigDecimal juros;
    private BigDecimal valorTotal;
    private LocalDate dtCriacao;
    private LocalDate dtAtualizacao;
    private Long formaPagamentoId;
    private Long fornecedorId;
    private Long filialId;
    private Long tipoId;
    private String dsFornecedor;
    private String dsFilial;
    private String dsTipo;
    private List<ParcelamentoDTO> parcelas;
    private List<NotaFiscalResponseDTO> notasFiscais;
}
