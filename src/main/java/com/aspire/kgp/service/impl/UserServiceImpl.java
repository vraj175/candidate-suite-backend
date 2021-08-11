package com.aspire.kgp.service.impl;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;
import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.CandidateDTO;
import com.aspire.kgp.dto.UserDTO;
import com.aspire.kgp.exception.APIException;
import com.aspire.kgp.exception.ValidateException;
import com.aspire.kgp.model.User;
import com.aspire.kgp.model.UserSearch;
import com.aspire.kgp.repository.UserRepository;
import com.aspire.kgp.service.LanguageService;
import com.aspire.kgp.service.MailService;
import com.aspire.kgp.service.RoleService;
import com.aspire.kgp.service.UserSearchService;
import com.aspire.kgp.service.UserService;
import com.aspire.kgp.util.CommonUtil;
import com.aspire.kgp.util.RestUtil;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class UserServiceImpl implements UserService {
  private static final Log log = LogFactory.getLog(UserServiceImpl.class);

  @Autowired
  UserRepository repository;

  @Autowired
  UserSearchService searchService;

  @Autowired
  RoleService roleService;

  @Autowired
  LanguageService languageService;

  @Autowired
  MailService mailService;

  @Autowired
  RestUtil restUtil;

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
  public User saveOrUpdatePartner(String galaxyId, String email, String password, boolean isLastLogin) {
    User user = findByEmail(email);
    if(user==null) {
      user = new User();
    }
    user.setRole(roleService.findByName(Constant.PARTNER));
    user.setEmail(email);
    user.setPassword(CommonUtil.hash(password));
    user.setGalaxyId(galaxyId);
    user.setLanguage(languageService.findByName(Constant.ENGLISH));
    if(isLastLogin) {
      user.setLastLogin(new Timestamp(System.currentTimeMillis()));
    }
    return saveorUpdate(user);
  }

  @Override
  @Transactional(value = TxType.REQUIRES_NEW)
  public boolean inviteUser(String candidateId, String language, String email, String[] bcc,
      User invitedBy, boolean removeDuplicate, HttpServletRequest request) {
    boolean response = false;
    String apiResponse = restUtil
        .newGetMethod(Constant.CONDIDATE_URL.replace("{candidateId}", candidateId));
    JsonObject json = (JsonObject) JsonParser.parseString(apiResponse);
    CandidateDTO candidateDTO =
        new Gson().fromJson(json.get("candidate"), new TypeToken<CandidateDTO>() {

          /**
           * 
           */
          private static final long serialVersionUID = 1L;
        }.getType());
    if (candidateDTO == null) {
      throw new APIException("Invalid Candidate Id");
    }

    User user = findByGalaxyId(candidateDTO.getContact().getId());
    if (user != null) {
      log.info("removing duplicate invite...");
      checkDuplicateInvite(user, candidateId, removeDuplicate);
    } else {
      log.info("insering new user...");
      user = new User();
      user.setRole(roleService.findByName(Constant.CANDIDATE));
      user.setEmail(email);
      user.setPassword(CommonUtil.hash(email));
      user.setGalaxyId(candidateDTO.getContact().getId());
      user.setPasswordReset(Boolean.TRUE);
    }
    user.setModifyDate(new Timestamp(System.currentTimeMillis()));
    user.setLanguage(languageService.findByName(language));
    user = saveorUpdate(user);

    try {
      log.info("insering new user search...");
      UserSearch userSearch = new UserSearch();
      userSearch.setSearchId(candidateDTO.getSearch().getId());
      userSearch.setCandidateId(candidateId);
      userSearch.setUser(user);
      userSearch.setInvitedBy(invitedBy);
      searchService.saveorUpdate(userSearch);

      UserDTO userDTO = candidateDTO.getContact();
      userDTO.setToken("");
      userDTO.setPrivateEmail(email);

      log.info("staring email sending...");
      if (user.isPasswordReset()) {
        // mail for add user or mail for invite
        mailService.sendEmail(email, bcc, Constant.INVITE_SUBJECT,
            mailService.getInviteEmailContent(request, userDTO, language), null);
      } else {
        // mail for add search
      }
      response = true;
    } catch (Exception e) {
      e.printStackTrace();
      throw new APIException("Error in send invite");
    }
    return response;
  }

  @Transactional(value = TxType.MANDATORY)
  private boolean checkDuplicateInvite(User user, String candidateId, boolean removeDuplicate) {
    UserSearch userSearch = searchService.findByUserAndCandidateId(user, candidateId);
    if (userSearch != null) {
      if (removeDuplicate) {
        searchService.deleteUserSearch(userSearch);
        return true;
      } else {
        log.info("Candidate already invited");
        throw new ValidateException("Candidate already invited");
      }
    }
    return false;
  }

  @Override
  public User findByEmail(String email) {
    return repository.findByEmailAndIsDeletedFalse(email);
  }

  @Override
  public UserDTO getContactDetails(String contactId) {
    String apiResponse = restUtil
        .newGetMethod(Constant.CONTACT_URL.replace("{contactId}", contactId));
    JsonObject json = (JsonObject) JsonParser.parseString(apiResponse);
    UserDTO userDTO =
        new Gson().fromJson(json, new TypeToken<UserDTO>() {

          /**
           * 
           */
          private static final long serialVersionUID = 1L;
        }.getType());
    if (userDTO == null) {
      throw new APIException("Invalid contactId");
    }
    return userDTO;
  }
  
  @Override
  public UserDTO getGalaxyUserDetails(String userId) {
    String apiResponse = restUtil
        .newGetMethod(Constant.USER_URL.replace("{userId}", userId));
    JsonObject json = (JsonObject) JsonParser.parseString(apiResponse);
    UserDTO userDTO =
        new Gson().fromJson(json, new TypeToken<UserDTO>() {

          /**
           * 
           */
          private static final long serialVersionUID = 1L;
        }.getType());
    if (userDTO == null) {
      throw new APIException("Invalid userId");
    }
    return userDTO;
  }

  @Override
  public User saveOrUpdatePartner(String userName, String password) {
    User user = findByEmail(userName);
    if (user == null || user.getRole().getName().equalsIgnoreCase(Constant.PARTNER)) {
      String auth = userName + ":" + password;
      try {
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
        String token = "Basic " + new String(encodedAuth, StandardCharsets.UTF_8);
        AuthenticationResultType authenticationResult =
            restUtil.validateCognitoWithAuthenticationToken(token);

        String accessToken = authenticationResult.getAccessToken();
        String authentication = restUtil.getUserDetails(accessToken);
        JsonObject userjson = new Gson().fromJson(authentication, JsonObject.class);
        log.info("add or update password");
        return saveOrUpdatePartner(userjson.get("id").getAsString(), userName, password, true);
      } catch (Exception e) {
        log.info("wrong partner craditionals or it was candidate");
      }
    }
    return null;
  }

}
