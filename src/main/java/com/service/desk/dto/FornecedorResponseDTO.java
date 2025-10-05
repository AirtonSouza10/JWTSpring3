package com.service.desk.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FornecedorResponseDTO {
    private Long id;
    private String nome;
    private String identificacao;
    private Integer tpIdentificacao;
    private List<TelefoneDTO> telefones;
    private List<EnderecoDTO> enderecos;
    private Boolean ativo;
    private String email;
}
