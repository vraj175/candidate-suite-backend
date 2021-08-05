package com.aspire.kgp.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import com.aspire.kgp.model.User;
import com.aspire.kgp.service.UserService;

@Repository
public class OAuthDao {
  
  @Autowired
  UserService service;
  
  public UserEntity getUserDetails(String username) {
    Collection<GrantedAuthority> grantedAuthoritiesList = new ArrayList<>();
    User users = service.findByEmail(username);
    UserEntity user = new UserEntity();
    user.setUsername(users.getEmail());
    user.setPassword(users.getPassword());
    List<UserEntity> list = Arrays.asList(user);
    if (list.size() > 0) {
      GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_SYSTEMADMIN");
      grantedAuthoritiesList.add(grantedAuthority);
      list.get(0).setGrantedAuthoritiesList(grantedAuthoritiesList);
      return list.get(0);
   }
   return null;
  } 
}
