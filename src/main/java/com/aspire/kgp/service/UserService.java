package com.aspire.kgp.service;

import com.aspire.kgp.model.User;

public interface UserService {
  User findById(Long id);

  User saveorUpdate(User user);

}
