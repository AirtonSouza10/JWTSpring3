package com.service.desk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.desk.entidade.Filial;

@Repository
public interface FilialRepository extends JpaRepository<Filial, Long> {
}
