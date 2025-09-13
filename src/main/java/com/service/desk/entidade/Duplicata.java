package com.service.desk.entidade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private BigDecimal valorTotal;
    
	
    @Temporal(TemporalType.DATE)
    private Date dtCriacao;
    
    @Temporal(TemporalType.DATE)
    private Date dtAtualizacao;
    
    @OneToMany(mappedBy = "duplicata", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Parcela> parcelas = new ArrayList<>();
    
}
