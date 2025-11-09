package com.service.desk.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RelatorioContasAbertasResponseDTO {
	private String filial;
    private List<RelatorioMesDTO> meses;
    private BigDecimal totalGeral;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RelatorioMesDTO {
        private String mesAno;
        private List<FornecedorValorDTO> fornecedores;
        private BigDecimal subtotal;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FornecedorValorDTO {
        private String fornecedor;
        private BigDecimal valor;
    }
}
