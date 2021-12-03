package com.aspire.kgp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aspire.kgp.model.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

  Contact findByGalaxyId(String galaxyId);

}
