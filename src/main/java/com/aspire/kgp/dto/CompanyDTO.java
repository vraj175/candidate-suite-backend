package com.aspire.kgp.dto;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.google.gson.annotations.SerializedName;

@JsonFilter("companyFilter")
public class CompanyDTO {
  private String id;
  private String name;
  private String description;
  private String website;
  @SerializedName("linkedin_url")
  private String linkedinUrl;
  private String news;

  public String getWebsite() {
    return website;
  }

  public void setWebsite(String website) {
    this.website = website;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLinkedinUrl() {
    return linkedinUrl;
  }

  public void setLinkedinUrl(String linkedinUrl) {
    this.linkedinUrl = linkedinUrl;
  }

  public String getNews() {
    return news;
  }

  public void setNews(String news) {
    this.news = news;
  }

  @Override
  public String toString() {
    return "CompanyDTO [id=" + id + ", name=" + name + ", description=" + description + ", website="
        + website + ", linkedinUrl=" + linkedinUrl + ", news=" + news + "]";
  }
}
