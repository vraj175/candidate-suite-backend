package com.aspire.kgp.dto;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class CommonDTO {

  @SerializedName("execution_credit")
  private String executionCredit;

  public String getExecutionCredit() {
    return executionCredit;
  }

  public void setExecutionCredit(String executionCredit) {
    this.executionCredit = executionCredit;
  }
  
  private List<UserDTO> partners;

  public List<UserDTO> getPartners() {
    return partners;
  }

  public void setPartners(List<UserDTO> partners) {
    this.partners = partners;
  }

}
