package com.service.desk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.desk.entidade.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findByNome(String nome);
}
