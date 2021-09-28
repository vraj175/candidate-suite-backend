package com.aspire.kgp.service;

import java.util.List;

import com.aspire.kgp.dto.CandidateDTO;
import com.aspire.kgp.dto.PositionProfileDTO;
import com.aspire.kgp.dto.SearchDTO;
import com.aspire.kgp.model.User;

public interface SearchService {

  public List<SearchDTO> getSearchListForUser(User user, String stage);
  
  public List<SearchDTO> getSearchList(String companyId, String stage);
  
  public List<CandidateDTO> getCandidateList(String searchId);
  
  public PositionProfileDTO getPositionProfileDetails(String searchId);
  
  public SearchDTO getsearchDetails(String searchId);
}
