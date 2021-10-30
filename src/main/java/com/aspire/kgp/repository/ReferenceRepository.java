package com.aspire.kgp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aspire.kgp.model.Reference;

public interface ReferenceRepository extends JpaRepository<Reference, Long> {

  List<Reference> findByContactId(String contactId);

  void save(Optional<Reference> referenceDatas);

}
