package com.aspire.kgp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aspire.kgp.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
