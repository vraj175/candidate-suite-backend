package com.aspire.kgp.service;

import java.util.List;

import com.aspire.kgp.model.Role;

public interface RoleService {

  Role saveorUpdate(Role role);

  List<Role> saveAll(List<Role> roles);

  List<Role> findAll();
  
  Role findByName(String name);

  String initializeData();
}
