package com.service.desk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TipoNotaResponseDTO {
    private Long id;
    private String descricao;
}
