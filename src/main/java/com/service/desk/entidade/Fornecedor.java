package com.service.desk.entidade;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fornecedores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fornecedor{

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fornecedor_seq")
    @SequenceGenerator(name = "fornecedor_seq", sequenceName = "fornecedor_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String nome;
    
    @Column(nullable = true)
    private String razao;

    @Column(nullable = false)
    private String identificacao;
    
    @Column(nullable = true)
    private Integer tpIdentificacao;
    
    @Column
    private String email;
    
    @Column(nullable = false)
    private Boolean ativo = true;
    
	private LocalDate dtInclusao;
	private LocalDate dtAtualizacao;
    
    @OneToMany(mappedBy = "fornecedor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Telefone> telefones;
    
    @OneToMany(mappedBy = "fornecedor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Endereco> enderecos;
    
}
