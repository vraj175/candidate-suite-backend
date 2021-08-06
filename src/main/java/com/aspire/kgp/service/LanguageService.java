package com.aspire.kgp.service;

import java.util.List;

import com.aspire.kgp.model.Language;

public interface LanguageService {

  Language saveorUpdate(Language language);

  List<Language> saveAll(List<Language> languages);

  List<Language> findAll();

  Language findByName(String name);

  String initializeData();
}
