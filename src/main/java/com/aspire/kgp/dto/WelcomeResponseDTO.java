package com.aspire.kgp.dto;

import java.util.List;

public class WelcomeResponseDTO {

  private String companyName;

  private String jobTitle;

  private String candidateName;

  private List<PartnerDTO> partners;

  public List<PartnerDTO> getPartners() {
    return partners;
  }

  public void setPartners(List<PartnerDTO> partners) {
    this.partners = partners;
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public String getJobTitle() {
    return jobTitle;
  }

  public void setJobTitle(String jobTitle) {
    this.jobTitle = jobTitle;
  }

  public String getCandidateName() {
    return candidateName;
  }

  public void setCandidateName(String candidateName) {
    this.candidateName = candidateName;
  }
}
