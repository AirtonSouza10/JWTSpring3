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
    Page<Duplicata> findAll(Pageable pageable);

    Page<Duplicata> findByDescricaoContaining(String numero, Pageable pageable);
    
    List<Duplicata> findByDescricaoContainingIgnoreCase(String descricao);
    
	@EntityGraph(attributePaths = {"parcelas"})
    List<Duplicata> findByFilialId(Long filialId);
    
    Page<Duplicata> findByIdIn(Set<Long> ids, Pageable pageable);
    
    List<Duplicata> findByIdIn(Set<Long> ids);

}
