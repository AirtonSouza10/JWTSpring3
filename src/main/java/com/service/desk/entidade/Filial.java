package com.service.desk.entidade;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "filial")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Filial{

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "filial_seq")
    @SequenceGenerator(name = "filial_seq", sequenceName = "filial_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String identificacao;
    
    @Column(nullable = false)
    private Integer tpIdentificacao;
    
    @Column
    private String email;
    
    @Column(nullable = false)
    private Boolean ativo = true;
    
	@Temporal(TemporalType.DATE)
	private Date dtInclusao;
	@Temporal(TemporalType.DATE)
	private Date dtAtualizacao;    
}
