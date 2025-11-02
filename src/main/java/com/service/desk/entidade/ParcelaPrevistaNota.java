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
@Table(name = "parcela_prevista_nota")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParcelaPrevistaNota{

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "parcela_prevista_nota_seq")
    @SequenceGenerator(name = "parcela_prevista_nota_seq", sequenceName = "parcela_prevista_nota_seq", allocationSize = 1)
    private Long id;
	
	private String numeroParcela;

    @ManyToOne
    @JoinColumn(name="nota_fiscal_id", nullable=false)
    private NotaFiscal notaFiscal;

    private LocalDate dtVencimentoPrevisto;

    private BigDecimal valorPrevisto;
    
}
