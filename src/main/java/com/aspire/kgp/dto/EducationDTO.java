package com.aspire.kgp.dto;

public class EducationDTO {

  private String id;
  private String school_name;
  private String degree_name;
  private String major;
  private String degree_year;
  private String position;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getSchool_name() {
    return school_name;
  }

  public void setSchool_name(String school_name) {
    this.school_name = school_name;
  }

  public String getDegree_name() {
    return degree_name;
  }

  public void setDegree_name(String degree_name) {
    this.degree_name = degree_name;
  }

  public String getMajor() {
    return major;
  }

  public void setMajor(String major) {
    this.major = major;
  }

  public String getDegree_year() {
    return degree_year;
  }

  public void setDegree_year(String degree_year) {
    this.degree_year = degree_year;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }
}
