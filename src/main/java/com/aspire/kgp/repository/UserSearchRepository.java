package com.aspire.kgp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aspire.kgp.model.User;
import com.aspire.kgp.model.UserSearch;

public interface UserSearchRepository extends JpaRepository<UserSearch, Long> {
  UserSearch findByUserAndCandidateIdAndIsDeletedFalse(User user, String candidateId);

}
