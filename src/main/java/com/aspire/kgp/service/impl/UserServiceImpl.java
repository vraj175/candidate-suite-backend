package com.aspire.kgp.service.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

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
import com.aspire.kgp.dto.ResetPasswordDTO;
import com.aspire.kgp.dto.UserDTO;
import com.aspire.kgp.exception.APIException;
import com.aspire.kgp.exception.NotFoundException;
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
import com.aspire.kgp.util.StaticContentsMultiLanguageUtil;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import freemarker.template.TemplateException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

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
  public User saveOrUpdatePartner(String galaxyId, String email, String password,
      boolean isLastLogin) {
    User user = findByEmail(email);
    if (user == null) {
      user = new User();
    }
    user.setRole(roleService.findByName(Constant.PARTNER));
    user.setEmail(email);
    user.setPassword(CommonUtil.hash(password));
    user.setGalaxyId(galaxyId);
    user.setLanguage(languageService.findByName(Constant.ENGLISH));
    if (isLastLogin) {
      user.setLastLogin(new Timestamp(System.currentTimeMillis()));
    }
    return saveorUpdate(user);
  }

  @Override
  @Transactional(value = TxType.REQUIRES_NEW)
  public boolean inviteUser(String candidateId, String language, String email, String[] bcc,
      User invitedBy, boolean removeDuplicate, HttpServletRequest request) {
    boolean response = false;
    String apiResponse =
        restUtil.newGetMethod(Constant.CONDIDATE_URL.replace("{candidateId}", candidateId));
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

    User userWithEmail = findByEmail(email);
    if (userWithEmail != null
        && !candidateDTO.getContact().getId().equalsIgnoreCase(userWithEmail.getGalaxyId())) {
      throw new APIException("Other Contact is already registered with same email");
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
      userDTO.setToken(generateJwtToken(email, email));
      userDTO.setEmail(email);

      log.info("staring email sending...");
      if (user.isPasswordReset()) {
        // mail for add user or mail for invite
        log.info("mail for add user or mail for invite");
        String languageCode = CommonUtil.getLanguageCode(language);
        Map<String, String> staticContentsMap = StaticContentsMultiLanguageUtil
            .getStaticContentsMap(languageCode, Constant.EMAILS_CONTENT_MAP);
        String mailSubject = staticContentsMap.get("candidate.suite.invitation.email.subject");
        mailService.sendEmail(email, bcc, mailSubject, mailService.getEmailContent(request, userDTO,
            staticContentsMap, Constant.CANDIDATE_INVITE_EMAIL_TEMPLATE), null);
      } else {
        log.info("mail for add search");
        // mail for add search
      }
      response = true;
    } catch (Exception e) {
      log.info(e);
      throw new APIException("Error in send invite");
    }
    return response;
  }

  private String generateJwtToken(String userName, String password) {
    log.info("generating Token for user...");
    Date dt = new Date();
    Calendar c = Calendar.getInstance();
    c.setTime(dt);
    c.add(Calendar.DATE, 1);
    dt = c.getTime();

    String auth = userName + ":" + password;
    String token = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));

    return Jwts.builder().setSubject("candidateSuite").setExpiration(dt)
        .setIssuer(Constant.FROM_MAIL).claim("authentication", "Basic " + token)
        .signWith(SignatureAlgorithm.HS512,
            Base64.getEncoder().encodeToString("candidateSuite-secret-key".getBytes()))
        .compact();
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
    String apiResponse =
        restUtil.newGetMethod(Constant.CONTACT_URL.replace("{contactId}", contactId));
    JsonObject json = (JsonObject) JsonParser.parseString(apiResponse);
    UserDTO userDTO = new Gson().fromJson(json, new TypeToken<UserDTO>() {

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
    String apiResponse = restUtil.newGetMethod(Constant.USER_URL.replace("{userId}", userId));
    JsonObject json = (JsonObject) JsonParser.parseString(apiResponse);
    UserDTO userDTO = new Gson().fromJson(json, new TypeToken<UserDTO>() {

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

  @Override
  @Transactional(value = TxType.REQUIRES_NEW)
  public boolean forgotPassword(HttpServletRequest request, String email) {
    boolean response = false;
    User user = findByEmail(email);
    if (user == null) {
      throw new NotFoundException("User is not available");
    }

    if (user.getRole().getName().equalsIgnoreCase(Constant.PARTNER)) {
      throw new APIException("you can't change the partner password from this app");
    }

    user.setPassword(CommonUtil.hash(email));
    user.setPasswordReset(Boolean.TRUE);
    user.setModifyDate(new Timestamp(System.currentTimeMillis()));
    user = saveorUpdate(user);

    UserDTO userDTO = getContactDetails(user.getGalaxyId());
    userDTO.setToken(generateJwtToken(email, email));
    userDTO.setEmail(email);
    log.info("staring email sending...");
    String languageCode = CommonUtil.getLanguageCode(user.getLanguage().getName());
    Map<String, String> staticContentsMap = StaticContentsMultiLanguageUtil
        .getStaticContentsMap(languageCode, Constant.EMAILS_CONTENT_MAP);
    String mailSubject = staticContentsMap.get("candidate.suite.forgot.email.subject");
    try {
      mailService.sendEmail(email, null, mailSubject, mailService.getEmailContent(request, userDTO,
          staticContentsMap, Constant.FORGOT_EMAIL_TEMPLATE), null);
      response = true;
    } catch (IOException | TemplateException e) {
      log.info(e);
      throw new APIException("Error in send Email");
    }

    return response;
  }

  @Override
  @Transactional(value = TxType.REQUIRES_NEW)
  public boolean resetPassword(HttpServletRequest request, ResetPasswordDTO resetPasswordDTO) {
    boolean response = false;
    User user = findByEmail(resetPasswordDTO.getEmail());
    if (user == null) {
      throw new NotFoundException("User is not available");
    }

    if (user.getRole().getName().equalsIgnoreCase(Constant.PARTNER)) {
      throw new APIException("you can't change the partner password from this app");
    }

    if (!CommonUtil.verifyHash(resetPasswordDTO.getOldPassword(), user.getPassword())) {
      throw new APIException("old password doesn't match");
    }

    user.setPassword(CommonUtil.hash(resetPasswordDTO.getNewPassword()));
    user.setPasswordReset(Boolean.FALSE);
    user.setModifyDate(new Timestamp(System.currentTimeMillis()));
    saveorUpdate(user);
    response = true;

    return response;
  }

}
