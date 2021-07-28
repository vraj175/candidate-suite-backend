package com.aspire.kgp.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.model.Role;
import com.aspire.kgp.repository.RoleRepository;
import com.aspire.kgp.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

  @Autowired
  RoleRepository repository;

  @Override
  public Role saveorUpdate(Role role) {
    return repository.save(role);
  }

  @Override
  public List<Role> saveAll(List<Role> roles) {
    return repository.saveAll(roles);
  }

  @Override
  public List<Role> findAll() {
    return repository.findAll();
  }
  
  @Override
  public Role findByName(String Name) {
    return repository.findByName(Name);
  }

  @Override
  public String initializeData() {
    if (findAll().isEmpty()) {
      List<Role> roles = new ArrayList<>();

      Role role1 = new Role();
      role1.setName(Constant.PARTNER);
      roles.add(role1);

      Role role2 = new Role();
      role2.setName(Constant.CANDIDATE);
      roles.add(role2);

      saveAll(roles);
      return "Data saved successfully";
    } else {
      return "Data already initialized";
    }
  }

}
