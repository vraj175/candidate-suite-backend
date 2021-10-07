package com.aspire.kgp.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;

import com.aspire.kgp.dto.CandidateDTO;
import com.aspire.kgp.dto.CandidateFeedbackDTO;

public interface CandidateService {

  public CandidateDTO getCandidateDetails(String candidateId);

  public ResponseEntity<byte[]> getAthenaReport(String pageSize, String locale, String contactId);

  public String addCandidateFeedback(String candidateId, String comments, String galaxyId, HttpServletRequest request);

  public List<CandidateFeedbackDTO> getCandidateFeedback(String candidateId);
}
