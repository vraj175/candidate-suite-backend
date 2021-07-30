package com.aspire.kgp.service;

import javax.servlet.http.HttpServletRequest;

import com.aspire.kgp.model.User;

public interface UserService {
  User findById(Long id);

  User saveorUpdate(User user);

  User findByGalaxyId(String galaxyId);

  User savePartner(String galaxyId, String email);

  User InviteUser(String candidateId, String language, String email, String[] BCC, User invitedBy,
      HttpServletRequest request);

}
