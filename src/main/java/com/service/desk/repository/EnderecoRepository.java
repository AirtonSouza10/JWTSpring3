package com.service.desk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.desk.entidade.Endereco;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
}
