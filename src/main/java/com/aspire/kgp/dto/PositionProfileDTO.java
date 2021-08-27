package com.aspire.kgp.dto;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.google.gson.annotations.SerializedName;

@JsonFilter("positionProfileFilter")
public class PositionProfileDTO {
  @SerializedName("is_degree_mandatory")
  private Boolean isDegreeMandatory;
  @SerializedName("is_approved_by_partner")
  private Boolean isApprovedByPartner;
  @SerializedName("is_years_of_experience_mandatory")
  private Boolean isYearsOfExperienceMandatory;
  @SerializedName("position_overview")
  private String positionOverview;
  @SerializedName("products_services_overview")
  private String productsServicesOverview;
  @SerializedName("professional_experience")
  private String professionalExperience;
  @SerializedName("years_of_experience")
  private String yearsOfExperience;
  @SerializedName("degree_name")
  private String degreeName;
  @SerializedName("certifications")
  private String certifications;
  @SerializedName("company")
  private CompanyDTO company;

  public Boolean getIsDegreeMandatory() {
    return isDegreeMandatory;
  }

  public void setIsDegreeMandatory(Boolean isDegreeMandatory) {
    this.isDegreeMandatory = isDegreeMandatory;
  }

  public Boolean getIsApprovedByPartner() {
    return isApprovedByPartner;
  }

  public void setIsApprovedByPartner(Boolean isApprovedByPartner) {
    this.isApprovedByPartner = isApprovedByPartner;
  }

  public Boolean getIsYearsOfExperienceMandatory() {
    return isYearsOfExperienceMandatory;
  }

  public void setIsYearsOfExperienceMandatory(Boolean isYearsOfExperienceMandatory) {
    this.isYearsOfExperienceMandatory = isYearsOfExperienceMandatory;
  }

  public String getPositionOverview() {
    return positionOverview;
  }

  public void setPositionOverview(String positionOverview) {
    this.positionOverview = positionOverview;
  }

  public String getProductsServicesOverview() {
    return productsServicesOverview;
  }

  public void setProductsServicesOverview(String productsServicesOverview) {
    this.productsServicesOverview = productsServicesOverview;
  }

  public String getProfessionalExperience() {
    return professionalExperience;
  }

  public void setProfessionalExperience(String professionalExperience) {
    this.professionalExperience = professionalExperience;
  }

  public String getYearsOfExperience() {
    return yearsOfExperience;
  }

  public void setYearsOfExperience(String yearsOfExperience) {
    this.yearsOfExperience = yearsOfExperience;
  }

  public String getDegreeName() {
    return degreeName;
  }

  public void setDegreeName(String degreeName) {
    this.degreeName = degreeName;
  }

  public String getCertifications() {
    return certifications;
  }

  public void setCertifications(String certifications) {
    this.certifications = certifications;
  }

  public CompanyDTO getCompany() {
    return company;
  }

  public void setCompany(CompanyDTO company) {
    this.company = company;
  }


}
