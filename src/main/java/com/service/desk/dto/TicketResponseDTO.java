package com.service.desk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TicketResponseDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private String categoria;
    private String sentimento;
}
