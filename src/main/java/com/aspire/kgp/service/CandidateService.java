package com.aspire.kgp.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;

import com.aspire.kgp.dto.CandidateDTO;

public interface CandidateService {

  public CandidateDTO getCandidateDetails(String candidateId);

  public ResponseEntity<byte[]> getAthenaReport(String pageSize, String locale, String contactId);

  public ResponseEntity<Object> saveFeedbackAndSendmail(HttpServletRequest resourceRequest);
}