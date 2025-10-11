package com.service.desk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.desk.entidade.Parcela;

@Repository
public interface ParcelaRepository extends JpaRepository<Parcela, Long> {
	List<Parcela> findByDuplicataId(Long duplicataId);
}
