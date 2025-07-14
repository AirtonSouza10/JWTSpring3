package com.service.desk.entidade;

import jakarta.persistence.Column;
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
@Table(name = "telefones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Telefone{

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "telefone_seq")
    @SequenceGenerator(name = "telefone_seq", sequenceName = "telefone_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String numero;

    @ManyToOne
    @JoinColumn(name = "tipo_id")
    private TelefoneTipo tpTelefone;
    
    @ManyToOne
    @JoinColumn(name = "fornecedor_id")
    private Fornecedor fornecedor;
}
