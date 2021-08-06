package com.aspire.kgp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomDetailsService implements UserDetailsService {
  @Autowired 
  OAuthDao oauthDao;

  @Override
  public CustomUser loadUserByUsername(final String username) throws UsernameNotFoundException {
     UserEntity userEntity = null;
     try {
        userEntity = oauthDao.getUserDetails(username);
        return new CustomUser(userEntity);
     } catch (Exception e) {
        e.printStackTrace();
        throw new UsernameNotFoundException("User " + username + " was not found in the database");
     }
  }
}
