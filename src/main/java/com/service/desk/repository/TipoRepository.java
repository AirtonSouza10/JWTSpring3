package com.service.desk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.desk.entidade.Tipo;

@Repository
public interface TipoRepository extends JpaRepository<Tipo, Long> {
}
