package com.service.desk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.desk.entidade.TelefoneTipo;

@Repository
public interface TelefoneTipoRepository extends JpaRepository<TelefoneTipo, Long> {
}
