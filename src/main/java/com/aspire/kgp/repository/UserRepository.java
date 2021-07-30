package com.aspire.kgp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aspire.kgp.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  User findByGalaxyId(String galaxyId);
}
