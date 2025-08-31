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
@Table(name = "nota_Tipo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotaTipo{

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nota_Tipo_seq")
    @SequenceGenerator(name = "nota_Tipo_seq", sequenceName = "nota_Tipo_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String descricao;
}
