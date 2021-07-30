package com.aspire.kgp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aspire.kgp.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Role findByName(String Name);
}
