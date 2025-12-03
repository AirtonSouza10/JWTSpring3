package com.service.desk.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.desk.entidade.Duplicata;

@Repository
public interface DuplicataRepository extends JpaRepository<Duplicata, Long> {
	@EntityGraph(attributePaths = {"parcelas","formaPagamento","fornecedor","filial","tipo"})
    Page<Duplicata> findAll(Pageable pageable);

	@EntityGraph(attributePaths = {"parcelas","formaPagamento","fornecedor","filial","tipo"})
    Page<Duplicata> findByDescricaoContaining(String numero, Pageable pageable);
    
	@EntityGraph(attributePaths = {"parcelas","formaPagamento","fornecedor","filial","tipo"})
    List<Duplicata> findByDescricaoContainingIgnoreCase(String descricao);
    
	@EntityGraph(attributePaths = {"parcelas","formaPagamento","fornecedor","filial","tipo"})
    List<Duplicata> findByFilialId(Long filialId);
    
	@EntityGraph(attributePaths = {"parcelas","formaPagamento","fornecedor","filial","tipo"})
    Page<Duplicata> findByIdIn(Set<Long> ids, Pageable pageable);
    
	@EntityGraph(attributePaths = {"parcelas","formaPagamento","fornecedor","filial","tipo"})
    List<Duplicata> findByIdIn(Set<Long> ids);

	@EntityGraph(attributePaths = {"parcelas","formaPagamento","fornecedor","filial","tipo"})
	List<Duplicata> findByFilialIdAndTipoId(Long filialId, Long tipoId);
}
