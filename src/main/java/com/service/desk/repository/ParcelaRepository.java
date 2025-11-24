package com.service.desk.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.service.desk.entidade.Parcela;

@Repository
public interface ParcelaRepository extends JpaRepository<Parcela, Long>, JpaSpecificationExecutor<Parcela>{
	List<Parcela> findByDuplicataId(Long duplicataId);
	
    List<Parcela> findByDtVencimento(LocalDate dtVencimento);
    
    List<Parcela> findByDtVencimentoBefore(LocalDate data);
    
    List<Parcela> findByNumeroParcelaContainingIgnoreCase(String numero);
    
    boolean existsByDuplicataIdAndStatusId(Long duplicataId, Long statusId);

}
