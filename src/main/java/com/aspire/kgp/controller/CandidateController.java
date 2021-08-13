package com.aspire.kgp.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.kgp.dto.WelcomeResponseDTO;
import com.aspire.kgp.util.CandidateUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

@RestController
@RequestMapping("/api/v1.0")
@Api(tags = {"Candidate"})
@SwaggerDefinition(tags = {@Tag(name = "Candidate", description = "Rest API For Candidate")})
public class CandidateController {

  static Log log = LogFactory.getLog(CandidateController.class.getName());

  @Autowired
  CandidateUtil candidateUtil;

  /***
   * 
   * @param searchId
   * @return
   */
  @ApiOperation(value = "Get Welcome Page Details")
  @GetMapping(value = {"/candidate/{candidateId}"})
  public WelcomeResponseDTO getWelcomePageDetails(@PathVariable("candidateId") String candidateId) {
    return candidateUtil.getWelcomePageDetails(candidateId);
  }
}
