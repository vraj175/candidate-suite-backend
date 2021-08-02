package com.aspire.kgp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aspire.kgp.model.User;
import com.aspire.kgp.model.UserSearch;
import com.aspire.kgp.repository.UserSearchRepository;
import com.aspire.kgp.service.UserSearchService;

@Service
public class UserSearchServiceImpl implements UserSearchService {

  @Autowired
  UserSearchRepository repository;

  @Override
  public UserSearch findByUserAndCandidateId(User user, String candidateId) {
    return repository.findByUserAndCandidateIdAndIsDeletedFalse(user, candidateId);
  }

  @Override
  public UserSearch deleteUserSearch(UserSearch userSearch) {
    userSearch.setDeleted(Boolean.TRUE);
    return saveorUpdate(userSearch);
  }

  @Override
  public UserSearch saveorUpdate(UserSearch userSearch) {
    return repository.save(userSearch);
  }

}
