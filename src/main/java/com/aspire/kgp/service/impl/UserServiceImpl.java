package com.aspire.kgp.service.impl;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.exception.APIException;
import com.aspire.kgp.model.User;
import com.aspire.kgp.repository.UserRepository;
import com.aspire.kgp.service.LanguageService;
import com.aspire.kgp.service.MailService;
import com.aspire.kgp.service.RoleService;
import com.aspire.kgp.service.UserService;
import com.aspire.kgp.util.CommonUtil;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  UserRepository repository;

  @Autowired
  RoleService roleService;

  @Autowired
  LanguageService languageService;

  @Autowired
  MailService mailService;

  @Override
  public User findById(Long id) {
    return repository.findById(id).orElse(null);
  }

  @Override
  public User saveorUpdate(User user) {
    return repository.save(user);
  }

  @Override
  public User findByGalaxyId(String galaxyId) {
    return repository.findByGalaxyId(galaxyId);
  }

  @Override
  public User savePartner(String galaxyId, String email) {
    User user = new User();
    user.setRole(roleService.findByName(Constant.PARTNER));
    user.setEmail(email);
    user.setPassword(CommonUtil.hash(email));
    user.setGalaxyId(galaxyId);
    user.setLanguage(languageService.findByName(Constant.ENGLISH));
    return saveorUpdate(user);
  }

  @Override
  @Transactional
  public User InviteUser(String contactId, String language, String email, String[] BCC,
      User invitedBy, HttpServletRequest request) {
    User response;
    try {
      User user = new User();
      // user.setRole(roleService.findByName(Constant.CANDIDATE));
      // user.setEmail(email);
      // user.setPassword(CommonUtil.hash(email));
      // user.setGalaxyId(contactId);
      // user.setLanguage(languageService.findByName(language));
      // user.setInvitedBy(invitedBy);

      response = user;

      mailService.sendEmail(email, BCC, Constant.INVITE_SUBJECT,
          mailService.getInviteEmailContent(request), null);
    } catch (Exception e) {
      throw new APIException("Error in send invite");
    }
    return response;
  }



}
