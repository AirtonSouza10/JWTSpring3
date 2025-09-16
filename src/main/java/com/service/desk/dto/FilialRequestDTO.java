package com.service.desk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FilialRequestDTO {
    private Long id;
    private String nome;
    private String identificacao;
    private String tpidentificacao;
    private String email;
}
