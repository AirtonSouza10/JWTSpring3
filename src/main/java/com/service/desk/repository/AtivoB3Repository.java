package com.service.desk.repository;

import com.service.desk.entidade.AtivoB3;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AtivoB3Repository extends JpaRepository<AtivoB3, Long> {
    Optional<AtivoB3> findBySymbol(String symbol);
}

