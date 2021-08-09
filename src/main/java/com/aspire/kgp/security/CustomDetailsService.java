package com.aspire.kgp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.exception.UnauthorizedAccessException;

@Service
public class CustomDetailsService implements UserDetailsService {
  @Autowired
  OAuthDao oauthDao;

  @Override
  public CustomUser loadUserByUsername(final String username) throws UsernameNotFoundException {
    UserEntity userEntity = null;
    try {
      userEntity = oauthDao.getUserDetails(username);
      if (userEntity != null)
        return new CustomUser(userEntity);
      throw new Exception();
    } catch (Exception e) {
      throw new UnauthorizedAccessException(Constant.INVALID_AUTHENTICATION);
    }
  }
}
