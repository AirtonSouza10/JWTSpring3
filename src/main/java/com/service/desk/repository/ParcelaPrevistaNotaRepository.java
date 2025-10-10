package com.service.desk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.service.desk.entidade.ParcelaPrevistaNota;

@Repository
public interface ParcelaPrevistaNotaRepository extends JpaRepository<ParcelaPrevistaNota, Long> {
	@Query("SELECT p FROM ParcelaPrevistaNota p WHERE p.notaFiscal.id = :notaFiscalId")
	List<ParcelaPrevistaNota> findByNotaFiscalId(Long notaFiscalId);
}
