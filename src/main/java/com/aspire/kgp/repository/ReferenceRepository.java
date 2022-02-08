package com.aspire.kgp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aspire.kgp.model.Reference;

@Repository
public interface ReferenceRepository extends JpaRepository<Reference, Long> {

  List<Reference> findByContactId(String contactId);

  Reference findByIdAndContactId(Long id, String contactId);
}
