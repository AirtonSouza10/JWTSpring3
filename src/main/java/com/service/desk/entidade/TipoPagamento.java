package com.service.desk.entidade;

import java.time.LocalDate;

import jakarta.persistence.Column;
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
@Table(name = "tipo_pagamento")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoPagamento{

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_pagamento_seq")
    @SequenceGenerator(name = "tipo_pagamento_seq", sequenceName = "tipo_pagamento_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String descricao;
	private LocalDate dtInclusao;
	private LocalDate dtAtualizacao;
}
