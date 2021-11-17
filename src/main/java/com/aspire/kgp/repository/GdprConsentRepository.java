package com.aspire.kgp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aspire.kgp.model.GdprConsent;

@Repository
public interface GdprConsentRepository extends JpaRepository<GdprConsent, Long> {

  GdprConsent findByContactId(String contactId);

}
