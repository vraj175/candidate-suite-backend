package com.aspire.kgp.service;

import com.aspire.kgp.model.User;
import com.aspire.kgp.model.UserSearch;

public interface UserSearchService {
  UserSearch findByUserAndCandidateId(User user, String candidateId);
  
  UserSearch saveorUpdate(UserSearch userSearch);
  
  UserSearch deleteUserSearch(UserSearch userSearch);
}
