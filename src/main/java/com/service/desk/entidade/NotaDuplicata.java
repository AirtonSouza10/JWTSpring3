package com.service.desk.entidade;

import java.math.BigDecimal;

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
@Table(name = "nota_duplicata")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotaDuplicata{

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nota_duplicata_seq")
    @SequenceGenerator(name = "nota_duplicata_seq", sequenceName = "nota_duplicata_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "nota_fiscal_id", nullable = false)
    private NotaFiscal notaFiscal;

    @ManyToOne
    @JoinColumn(name = "duplicata_id", nullable = false)
    private Duplicata duplicata;
    
    private BigDecimal valorRateado;
    
}
