package com.service.desk.entidade;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "nota_fiscal")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotaFiscal{

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nota_fiscal_seq")
    @SequenceGenerator(name = "nota_fiscal_seq", sequenceName = "nota_fiscal_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false, length = 20)
    private String numero;

    @Column(nullable = false, length = 5)
    private String serie;

    @Column(nullable = false, unique = true, length = 44)
    private String chave;
    
    @Column(length = 500)
    private String descricaoObs;

    @Column(nullable = false)
    private BigDecimal valorTotal;

    @Column(nullable = true)
    private BigDecimal valorDesconto;

    @Column(nullable = true)
    private BigDecimal valorIcms;

    @Column(nullable = true)
    private BigDecimal valorJuros;

    @Column(nullable = true)
    private BigDecimal valorMulta;
    
    @Column(nullable = true)
    private BigDecimal valorIpi;

    @Temporal(TemporalType.DATE)
    private Date dtCompra;
    
	@Temporal(TemporalType.DATE)
	private Date dtInclusao;
	@Temporal(TemporalType.DATE)
	private Date dtAtualizacao;

    @ManyToOne
    @JoinColumn(name = "fornecedor_id", nullable = false)
    private Fornecedor fornecedor;
    
    @ManyToOne
    @JoinColumn(name = "nota_tipo_id", nullable = false)
    private TipoNota tipo;
    
    @ManyToOne
    @JoinColumn(name = "pessoa_id", nullable = true)
    private Pessoa pessoa;
    
    @ManyToOne
    @JoinColumn(name = "filial_id", nullable = false)
    private Filial filial;
    
    @ManyToOne
    @JoinColumn(name = "forma_pagamento_id", nullable = true)
    private FormaPagamento formaPagamento;
    
    @OneToMany(mappedBy = "notaFiscal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParcelaPrevistaNota> parcelasPrevistas = new ArrayList<>();
}
