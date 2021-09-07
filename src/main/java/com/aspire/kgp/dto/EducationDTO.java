package com.aspire.kgp.dto;

import com.google.gson.annotations.SerializedName;

public class EducationDTO {

  private String id;
  @SerializedName("school_name")
  private String schoolName;
  @SerializedName("degree_name")
  private String degreeName;
  private String major;
  @SerializedName("degree_year")
  private String degreeYear;
  private String position;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public String getSchoolName() {
    return schoolName;
  }

  public void setSchoolName(String schoolName) {
    this.schoolName = schoolName;
  }

  public String getDegreeName() {
    return degreeName;
  }

  public void setDegreeName(String degreeName) {
    this.degreeName = degreeName;
  }

  public String getMajor() {
    return major;
  }

  public void setMajor(String major) {
    this.major = major;
  }

  public String getDegreeYear() {
    return degreeYear;
  }

  public void setDegreeYear(String degreeYear) {
    this.degreeYear = degreeYear;
  }


}
