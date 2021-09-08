package com.aspire.kgp.dto;

import com.google.gson.annotations.SerializedName;

public class DocumentDTO {
  private String id;
  @SerializedName("file_name")
  private String fileName;
  @SerializedName("created_at")
  private String createdAt;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

}
