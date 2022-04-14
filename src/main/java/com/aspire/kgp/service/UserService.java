package com.aspire.kgp.service;

import javax.servlet.http.HttpServletRequest;

import com.aspire.kgp.dto.ResetPasswordDTO;
import com.aspire.kgp.dto.UserDTO;
import com.aspire.kgp.model.User;

public interface UserService {
  User findById(Long id);

  User saveorUpdate(User user);

  User findByGalaxyId(String galaxyId);

  User saveOrUpdatePartner(String galaxyId, String email, String password, boolean isLastLogin);

  User findByEmail(String email);

  boolean inviteUser(String candidateId, String language, String email, String[] bcc,
      User invitedBy, boolean removeDuplicate, HttpServletRequest request);

  UserDTO getContactDetails(String contactId);

  UserDTO getGalaxyUserDetails(String userId);

  User saveOrUpdatePartner(String userName, String password);

  boolean forgotPassword(HttpServletRequest request, String email);

  boolean resetPassword(HttpServletRequest request, ResetPasswordDTO resetPasswordDTO);

  boolean isContactPassReset(String username, String password);
}
