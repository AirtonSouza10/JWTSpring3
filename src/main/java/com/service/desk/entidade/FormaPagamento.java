package com.service.desk.entidade;

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
@Table(name = "forma_pagamento")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormaPagamento{

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "forma_pagamento_seq")
    @SequenceGenerator(name = "forma_pagamento_seq", sequenceName = "forma_pagamento_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String descricao;
    
    @Column(nullable = false)
    private Integer qtdeParcelas;
}
