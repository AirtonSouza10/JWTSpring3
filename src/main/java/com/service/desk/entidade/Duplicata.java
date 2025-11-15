package com.service.desk.entidade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "duplicata")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Duplicata{

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "duplicata_seq")
    @SequenceGenerator(name = "duplicata_seq", sequenceName = "duplicata_seq", allocationSize = 1)
    private Long id;

	private String descricao;
	
    private BigDecimal valor;
    private BigDecimal desconto;
    private BigDecimal multa;
    private BigDecimal juros;
    private BigDecimal valorTotal;
    
	
    private LocalDate dtCriacao;
    private LocalDate dtAtualizacao;
    
    @ManyToOne
    @JoinColumn(name = "forma_pagamento_id", nullable = true)
    private FormaPagamento formaPagamento;
    
    @ManyToOne
    @JoinColumn(name = "fornecedor_id", nullable = true)
    private Fornecedor fornecedor;
    
    @ManyToOne
    @JoinColumn(name = "filial_id", nullable = true)
    private Filial filial;
    
    @ManyToOne
    @JoinColumn(name = "tipo_id", nullable = true)
    private Tipo tipo;
    
    @OneToMany(mappedBy = "duplicata", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Parcela> parcelas = new ArrayList<>();
    
    @OneToMany(mappedBy = "duplicata", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NotaFiscal> notasFiscais = new ArrayList<>();
    
}
