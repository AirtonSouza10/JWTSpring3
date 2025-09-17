package com.service.desk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.desk.entidade.Telefone;

@Repository
public interface TelefoneRepository extends JpaRepository<Telefone, Long> {
}
