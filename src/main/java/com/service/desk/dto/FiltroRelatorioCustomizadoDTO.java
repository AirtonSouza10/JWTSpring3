package com.service.desk.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FiltroRelatorioCustomizadoDTO {
    private Long idFilial;
    private Long idStatusConta;
    private Long idFornecedor;
    private Long idTipoNota;
    private Long idTipoDuplicata;
    private Long idTipoPagamento;

    private LocalDate dataInicial;
    private LocalDate dataFinal;
}
