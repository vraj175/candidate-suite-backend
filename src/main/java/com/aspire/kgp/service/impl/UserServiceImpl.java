package com.aspire.kgp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aspire.kgp.model.User;
import com.aspire.kgp.repository.UserRepository;
import com.aspire.kgp.service.UserService;

@Service
public class UserServiceImpl implements UserService {
  
  @Autowired
  UserRepository repository;

  @Override
  public User findById(Long id) {
    return repository.findById(id).orElse(null);
  }

  @Override
  public User saveorUpdate(User user) {
    return repository.save(user);
  }

 
}
