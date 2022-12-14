package com.aspire.kgp.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.aspire.kgp.dto.CandidateDTO;
import com.aspire.kgp.dto.CompanyDTO;
import com.aspire.kgp.dto.DocumentDTO;

public interface CompanyService {
  public List<CompanyDTO> getCompanyList(String stage);

  public CandidateDTO getCompanyInfoDetails(String candidateId, String timeZone);

  public List<CompanyDTO> getListOfCompany(String companyName);

  public String addNewCompany(String companyData);

  public List<DocumentDTO> getDocumentAttchment(String companyId);

  public ResponseEntity<Object> uploadCompanyAttachment(MultipartFile file, String companyId,
      HttpServletRequest request);
}
