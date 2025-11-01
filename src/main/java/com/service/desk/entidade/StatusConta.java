package com.service.desk.entidade;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "status_conta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusConta implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_status_conta")
    @SequenceGenerator(name = "status_conta_seq", sequenceName = "status_conta_seq", allocationSize = 1)
	private Long id;

	private String descricao;
	private LocalDate dtInclusao;
	private LocalDate dtAtualizacao;	
}
