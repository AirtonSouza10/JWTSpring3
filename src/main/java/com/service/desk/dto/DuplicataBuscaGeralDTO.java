package com.service.desk.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DuplicataBuscaGeralDTO {

    private Long duplicataId;
    private String descricaoDuplicata;

    private Long filialId;
    private String dsFilial;

    private Long fornecedorId;
    private String dsFornecedor;

    private List<ParcelaResumoDTO> parcelas;
    private List<NotaFiscalResumoDTO> notasFiscais;
}
