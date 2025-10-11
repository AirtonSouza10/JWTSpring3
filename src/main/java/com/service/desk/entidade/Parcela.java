package com.service.desk.entidade;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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

    private BigDecimal valorTotal;

    @Temporal(TemporalType.DATE)
    private Date dtVencimento;
    
    @Temporal(TemporalType.DATE)
    private Date dtCriacao;
    
    @Temporal(TemporalType.DATE)
    private Date dtAtualizacao;

    @ManyToOne
    @JoinColumn(name = "duplicata_id", nullable = false)
    private Duplicata duplicata;
    
}
