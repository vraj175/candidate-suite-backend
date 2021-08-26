package com.aspire.kgp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.model.Language;
import com.aspire.kgp.model.Role;
import com.aspire.kgp.model.User;

public class CustomTestData {
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
}
