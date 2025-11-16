package com.service.desk.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RelatorioParcelasPagasPorTipoDTO {
	private String filial;
	private String identificacao;
    private String tipoTitulo;
    private List<ParcelaPagaDTO> parcelas;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ParcelaPagaDTO {
        private String descricao;
        private LocalDate dataPagamento;
        private String dataPagamentoFormatado;
        private BigDecimal valorPago;
        private String valorPagoFormatado;
        private String fornecedor;
    }
}
