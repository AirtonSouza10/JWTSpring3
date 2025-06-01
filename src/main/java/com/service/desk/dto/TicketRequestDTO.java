package com.service.desk.dto;

import jakarta.validation.constraints.NotBlank;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketRequestDTO {
    @NotBlank(message = "O campo 'titulo' é obrigatório")
    private String titulo;
    @NotBlank(message = "O campo 'descrição' é obrigatório")
    private String descricao;
    private String categoria;
    private String sentimento;
}
