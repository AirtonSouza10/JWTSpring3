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
@Table(name = "tipo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tipo{

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_seq")
    @SequenceGenerator(name = "tipo_seq", sequenceName = "tipo_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String descricao;
 
}
