package com.service.desk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponseDTO {

	private String login;
	private String senha;
	private String identificacao;
	
    // Dados da pessoa
	private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String tpPessoa;
    private boolean ativo;

}
