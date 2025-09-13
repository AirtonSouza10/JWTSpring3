package com.service.desk.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.service.desk.enumerator.StatusContaEnum;

import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
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
import lombok.Data;

@Entity
@Table(name = "conta_pagar")
@AllArgsConstructor
@Data
@SequenceGenerator(name = "seq_conta_pagar", sequenceName = "seq_conta_pagar", allocationSize = 1, initialValue = 1)
public class ContaPagar implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_conta_pagar")
	private Long id;

	private String descricao;

	@Enumerated(EnumType.STRING)
	private StatusContaEnum status;

	@Temporal(TemporalType.DATE)
	private Date dtVencimento;
	@Temporal(TemporalType.DATE)
	private Date dtPagamento;	@Temporal(TemporalType.DATE)
	private Date dtInclusao;
	@Temporal(TemporalType.DATE)
	private Date dtAtualizacao;
	private BigDecimal valorTotal;
	private BigDecimal valorDesconto;
	private BigDecimal juros;
	private BigDecimal multa;

	@ManyToOne(targetEntity = Pessoa.class)
	@JoinColumn(name = "pessoa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "pessoa_fk"))
	private Pessoa pessoa;

	@ManyToOne(targetEntity = Pessoa.class)
	@JoinColumn(name = "forn_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "forn_fk"))
	private Fornecedor fornecedor;
	
	
}
