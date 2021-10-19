package com.aspire.kgp.service.impl;

import java.sql.Timestamp;
import java.util.List;

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
  public UserSearch findByUserAndSearchId(User user, String searchId) {
    return repository.findByUserAndSearchIdAndIsDeletedFalse(user, searchId);
  }

  @Override
  public UserSearch findByCandidateId(String candidateId) {
    return repository.findByCandidateIdAndIsDeletedFalse(candidateId);
  }

  @Override
  public UserSearch deleteUserSearch(UserSearch userSearch) {
    userSearch.setModifyDate(new Timestamp(System.currentTimeMillis()));
    userSearch.setDeleted(Boolean.TRUE);
    return saveorUpdate(userSearch);
  }

  @Override
  public UserSearch saveorUpdate(UserSearch userSearch) {
    return repository.save(userSearch);
  }

  @Override
  public List<UserSearch> findByUser(User user) {
    return repository.findByUserAndIsDeletedFalse(user);
  }

  @Override
  public List<UserSearch> findByIsDeletedFalse() {
    return repository.findByIsDeletedFalse();
  }

}
