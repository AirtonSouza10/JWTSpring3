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
@Table(name = "telefone_Tipos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TelefoneTipo{

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "telefone_tipo_seq")
    @SequenceGenerator(name = "telefone_tipo_seq", sequenceName = "telefone_tipo_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String descricao;
}
