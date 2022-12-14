package com.aspire.kgp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aspire.kgp.model.User;
import com.aspire.kgp.model.UserSearch;

public interface UserSearchRepository extends JpaRepository<UserSearch, Long> {
  UserSearch findByUserAndCandidateIdAndIsDeletedFalse(User user, String candidateId);
  
  UserSearch findByUserAndSearchIdAndIsDeletedFalse(User user, String searchId);
  
  UserSearch findByCandidateIdAndIsDeletedFalse(String candidateId);

  List<UserSearch> findByUserAndIsDeletedFalse(User user);
  
  List<UserSearch> findByIsDeletedFalse();
}
