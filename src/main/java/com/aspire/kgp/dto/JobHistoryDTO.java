package com.aspire.kgp.dto;

import com.google.gson.annotations.SerializedName;

public class JobHistoryDTO {

  private String id;
  private String title;
  @SerializedName(value = "start_year", alternate = {"start_date"})
  private String startYear;
  @SerializedName(value = "end_year", alternate = {"end_date"})
  private String endYear;
  private String position;
  private CompanyDTO company;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getStartYear() {
    return startYear;
  }

  public void setStartYear(String startYear) {
    this.startYear = startYear;
  }

  public String getEndYear() {
    return endYear;
  }

  public void setEndYear(String endYear) {
    this.endYear = endYear;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public CompanyDTO getCompany() {
    return company;
  }

  public void setCompany(CompanyDTO company) {
    this.company = company;
  }

}
