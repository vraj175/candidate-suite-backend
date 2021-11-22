package com.aspire.kgp.service;

import java.util.List;

import com.aspire.kgp.dto.CandidateDTO;
import com.aspire.kgp.dto.CompanyDTO;
import com.aspire.kgp.dto.DocumentDTO;

public interface CompanyService {
  public List<CompanyDTO> getCompanyList(String stage);

  public CandidateDTO getCompanyInfoDetails(String candidateId);

  public List<CompanyDTO> getListOfCompany(String companyName);

  public String addNewCompany(String companyData);

  public List<DocumentDTO> getDocumentAttchment(String companyId);
}
