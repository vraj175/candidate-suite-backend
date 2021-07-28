package com.aspire.kgp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aspire.kgp.model.Language;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
  Language findByName(String Name);
}
