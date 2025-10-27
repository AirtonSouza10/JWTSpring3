package com.service.desk.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.desk.entidade.Duplicata;

@Repository
public interface DuplicataRepository extends JpaRepository<Duplicata, Long> {
    Page<Duplicata> findAll(Pageable pageable);

    Page<Duplicata> findByDescricaoContaining(String numero, Pageable pageable);
    
    List<Duplicata> findByDescricaoContaining(String descricao);
}
