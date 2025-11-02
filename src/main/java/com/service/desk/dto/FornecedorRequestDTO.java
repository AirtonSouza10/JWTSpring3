package com.service.desk.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FornecedorRequestDTO {
    private Long id;
    private String nome;
    private String razao;
    private String identificacao;
    private Integer tpIdentificacao;
    private String email;
    private Boolean ativo;
    private List<TelefoneDTO> telefones;
    private List<EnderecoDTO> enderecos;
}
