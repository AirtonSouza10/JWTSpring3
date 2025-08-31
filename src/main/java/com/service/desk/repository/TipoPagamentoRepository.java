package com.service.desk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.desk.entidade.TipoPagamento;

@Repository
public interface TipoPagamentoRepository extends JpaRepository<TipoPagamento, Long> {
}
