package com.aspire.kgp.service;

import java.util.List;

import com.aspire.kgp.dto.PickListDTO;

public interface PickListService {

  public List<String> getEducationDegrees();

  public List<PickListDTO> getReferencesType();

  public List<PickListDTO> getCountries();

  public List<PickListDTO> getListOfIndustries();
}
