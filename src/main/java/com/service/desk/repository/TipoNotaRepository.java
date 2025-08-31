package com.service.desk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.desk.entidade.TipoNota;

@Repository
public interface TipoNotaRepository extends JpaRepository<TipoNota, Long> {
}
