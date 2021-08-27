package com.aspire.kgp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.model.Language;
import com.aspire.kgp.model.Role;
import com.aspire.kgp.model.User;
import com.aspire.kgp.model.UserSearch;

public class CustomTestData {
  
  public static MockHttpServletRequest getRequest() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    return request;
  }
  
  public static Language getLanguage() {
    Timestamp t1 = new Timestamp(System.currentTimeMillis());

    Language language = new Language();
    language.setId(Long.MIN_VALUE);
    language.setName(Constant.TEST);
    language.setCreatedDate(t1);
    language.setModifyDate(t1);

    return language;
  }

  public static List<Language> getLanguages() {
    List<Language> languages = new ArrayList<>();
    languages.add(getLanguage());
    return languages;
  }
  
  public static Role getRole() {
    Timestamp t1 = new Timestamp(System.currentTimeMillis());

    Role role = new Role();
    role.setId(Long.MIN_VALUE);
    role.setName(Constant.TEST);
    role.setCreatedDate(t1);
    role.setModifyDate(t1);

    return role;
  }

  public static List<Role> getRoles() {
    List<Role> roles = new ArrayList<>();
    roles.add(getRole());
    return roles;
  }
  
  public static User getUser() {
    Timestamp t1 = new Timestamp(System.currentTimeMillis());

    User user = new User();
    user.setId(Long.MIN_VALUE);
    user.setCreatedDate(t1);
    user.setModifyDate(t1);
    user.setGalaxyId(Constant.TEST);
    user.setDeleted(Boolean.FALSE);
    user.setEmail(Constant.TEST);
    user.setLastLogin(t1);
    user.setPassword(Constant.TEST);
    user.setPasswordReset(Boolean.FALSE);
    user.setLanguage(getLanguage());
    user.setRole(getRole());

    return user;
  }

  public static List<User> getUsers() {
    List<User> users = new ArrayList<>();
    users.add(getUser());
    return users;
  }
  
  public static UserSearch getUserSearch() {
    Timestamp t1 = new Timestamp(System.currentTimeMillis());

    UserSearch userSearch = new UserSearch();
    userSearch.setId(Long.MIN_VALUE);
    userSearch.setCreatedDate(t1);
    userSearch.setModifyDate(t1);
    userSearch.setDeleted(Boolean.FALSE);
    userSearch.setCandidateId(Constant.TEST);
    userSearch.setInvitedBy(getUser());
    userSearch.setSearchId(Constant.TEST);
    
    return userSearch;
  }
}
