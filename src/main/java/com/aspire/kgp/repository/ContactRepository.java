package com.aspire.kgp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aspire.kgp.model.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {

  Contact findByGalaxyId(String contactId);

}
