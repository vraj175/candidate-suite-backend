package com.aspire.kgp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aspire.kgp.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
  Role findByName(String Name);
}
