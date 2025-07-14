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
    private String identificacao;
    private String tpIdentificacao;
    private String email;
    private List<TelefoneDTO> telefones;
    private List<EnderecoDTO> enderecos;
}
