package com.aspire.kgp.service;

import java.util.List;

import com.aspire.kgp.dto.CandidateDTO;
import com.aspire.kgp.dto.CompanyDTO;

public interface CompanyService {
  public List<CompanyDTO> getCompanyList(String stage);

  public CandidateDTO getCompanyInfoDetails(String candidateId);

  public List<CompanyDTO> getListOfCompany(String companyName);
}
