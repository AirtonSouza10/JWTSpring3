package com.service.desk.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.desk.entidade.NotaFiscal;

@Repository
public interface NotaFiscalRepository extends JpaRepository<NotaFiscal, Long> {
    List<NotaFiscal> findByNumeroAndSerieAndFornecedorId(String numero, String serie, Long fornecedorId);
    
	List<NotaFiscal> findByNumeroAndSerieAndFornecedorIdAndIdNot(String numero, String serie, Long fornecedorId,
			Long id);
	
	List<NotaFiscal> findByNumeroAndFornecedorId(String numero, Long fornecedorId);
	
    Page<NotaFiscal> findByNumeroContainingAndFornecedorId(String numero, Long fornecedorId, Pageable pageable);

    Page<NotaFiscal> findAll(Pageable pageable);
}
