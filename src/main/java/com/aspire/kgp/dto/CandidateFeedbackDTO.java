package com.aspire.kgp.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.google.gson.annotations.SerializedName;

@JsonFilter("candidateFeedbackFilter")
public class CandidateFeedbackDTO {

  private String id;
  @SerializedName("candidate_id")
  private String candidateId;
  private String comments;
  @SerializedName("created_by")
  private String createdBy;
  @SerializedName("created_at")
  private String createdAt;
  @SerializedName("updated_at")
  private String updatedAt;
  private List<CandidateFeedbackRepliesDTO> replies;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCandidateId() {
    return candidateId;
  }

  public void setCandidateId(String candidateId) {
    this.candidateId = candidateId;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public String getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(String updatedAt) {
    this.updatedAt = updatedAt;
  }

  public List<CandidateFeedbackRepliesDTO> getReplies() {
    return replies;
  }

  public void setReplies(List<CandidateFeedbackRepliesDTO> replies) {
    this.replies = replies;
  }

  @Override
  public String toString() {
    return "CandidateFeedbackDTO [id=" + id + ", candidateId=" + candidateId + ", comments="
        + comments + ", createdBy=" + createdBy + ", createdAt=" + createdAt + ", updatedAt="
        + updatedAt + ", replies=" + replies + "]";
  }

}
