package com.aspire.kgp.service;

import java.util.List;

import com.aspire.kgp.model.User;
import com.aspire.kgp.model.UserSearch;

public interface UserSearchService {
  UserSearch findByUserAndCandidateId(User user, String candidateId);
  
  UserSearch findByUserAndSearchId(User user, String searchId);
  
  UserSearch findByCandidateId(String candidateId);
  
  UserSearch saveorUpdate(UserSearch userSearch);
  
  UserSearch deleteUserSearch(UserSearch userSearch);
  
  List<UserSearch> findByUser(User user);
  
  List<UserSearch> findByIsDeletedFalse();
}
