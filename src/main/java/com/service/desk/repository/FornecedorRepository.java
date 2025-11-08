package com.service.desk.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.desk.entidade.Fornecedor;

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
	Fornecedor findByIdentificacao(String identificacao);
	
    List<Fornecedor> findByIdentificacaoAndIdNot(String identificacao, Long id);
    
    Page<Fornecedor> findByNomeContainingIgnoreCaseOrIdentificacaoContaining(String nome, String identificacao, Pageable pageable);

}
