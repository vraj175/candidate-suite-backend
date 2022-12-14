package com.aspire.kgp.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.model.Language;
import com.aspire.kgp.repository.LanguageRepository;
import com.aspire.kgp.service.LanguageService;

@Service
public class LanguageServiceImpl implements LanguageService {

  @Autowired
  LanguageRepository repository;

  @Override
  public Language saveorUpdate(Language language) {
    return repository.save(language);
  }

  @Override
  public List<Language> saveAll(List<Language> languages) {
    return repository.saveAll(languages);
  }

  @Override
  public List<Language> findAll() {
    return repository.findAll();
  }

  @Override
  public Language findByName(String name) {
    return repository.findByName(name);
  }

  @Override
  public String initializeData() {
    if (findAll().isEmpty()) {
      List<Language> languages = new ArrayList<>();

      Language language1 = new Language();
      language1.setName(Constant.ENGLISH_CODE);
      languages.add(language1);

      Language language2 = new Language();
      language2.setName(Constant.SPANISH_CODE);
      languages.add(language2);

      Language language3 = new Language();
      language3.setName(Constant.PORTUGUESE_CODE);
      languages.add(language3);

      saveAll(languages);
      return Constant.DATA_SAVED;
    } else {
      return Constant.DATA_ALREADY_INITIALIZED;
    }
  }

}
