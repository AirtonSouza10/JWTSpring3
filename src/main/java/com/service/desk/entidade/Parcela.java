package com.service.desk.entidade;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "parcela")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Parcela{

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "parcela_seq")
    @SequenceGenerator(name = "parcela_seq", sequenceName = "parcela_seq", allocationSize = 1)
    private Long id;
	
	private String numeroParcela;
    private BigDecimal valorTotal;
    private LocalDate dtVencimento;
    private LocalDate dtCriacao;
    private LocalDate dtAtualizacao;
    
    private LocalDate dtPagamento;
    private LocalDate dtBaixa;
    private String observacao;
    private BigDecimal valorPago;

    @ManyToOne
    @JoinColumn(name = "duplicata_id", nullable = false)
    private Duplicata duplicata;
    
    @ManyToOne
    @JoinColumn(name = "status_conta_id")
    private StatusConta status;
    
    @ManyToOne
    @JoinColumn(name = "tipo_pagamento_id")
    private TipoPagamento tipoPagamento;
    
}
