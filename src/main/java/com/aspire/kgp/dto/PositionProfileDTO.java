package com.aspire.kgp.dto;

import java.util.List;

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

  @SerializedName("cs_location_weather")
  private List<LocationDTO> weather;
  @SerializedName("cs_location_placeofinterest")
  private List<LocationDTO> placeofinterest;
  @SerializedName("cs_location_schoolsandcolleges")
  private List<LocationDTO> schoolsandcolleges;
  @SerializedName("cs_location_restaurantsandshopping")
  private List<LocationDTO> restaurantsandshopping;
  @SerializedName("cs_location_realestate")
  private List<LocationDTO> realestate;

  public List<LocationDTO> getWeather() {
    return weather;
  }

  public void setWeather(List<LocationDTO> weather) {
    this.weather = weather;
  }

  public List<LocationDTO> getPlaceofinterest() {
    return placeofinterest;
  }

  public void setPlaceofinterest(List<LocationDTO> placeofinterest) {
    this.placeofinterest = placeofinterest;
  }

  public List<LocationDTO> getSchoolsandcolleges() {
    return schoolsandcolleges;
  }

  public void setSchoolsandcolleges(List<LocationDTO> schoolsandcolleges) {
    this.schoolsandcolleges = schoolsandcolleges;
  }

  public List<LocationDTO> getRestaurantsandshopping() {
    return restaurantsandshopping;
  }

  public void setRestaurantsandshopping(List<LocationDTO> restaurantsandshopping) {
    this.restaurantsandshopping = restaurantsandshopping;
  }

  public List<LocationDTO> getRealestate() {
    return realestate;
  }

  public void setRealestate(List<LocationDTO> realestate) {
    this.realestate = realestate;
  }

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
