package com.service.desk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.desk.entidade.Endereco;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
	List<Endereco> findByFornecedorId(Long idFornecedor);
}
