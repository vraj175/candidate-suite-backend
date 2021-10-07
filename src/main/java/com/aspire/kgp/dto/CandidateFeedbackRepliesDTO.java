package com.aspire.kgp.dto;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.google.gson.annotations.SerializedName;

@JsonFilter("candidateFeedbackRepliesFilter")
public class CandidateFeedbackRepliesDTO {
  private String id;
  @SerializedName("candidate_id")
  private String candidateId;
  @SerializedName("comment_id")
  private String commentId;
  private String reply;
  @SerializedName("created_by")
  private String createdBy;
  @SerializedName("created_at")
  private String createdAt;
  @SerializedName("updated_at")
  private String updatedAt;

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

  public String getCommentId() {
    return commentId;
  }

  public void setCommentId(String commentId) {
    this.commentId = commentId;
  }

  public String getReply() {
    return reply;
  }

  public void setReply(String reply) {
    this.reply = reply;
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

  @Override
  public String toString() {
    return "CandidateFeedbackRepliesDTO [id=" + id + ", candidateId=" + candidateId + ", commentId="
        + commentId + ", reply=" + reply + ", createdBy=" + createdBy + ", createdAt=" + createdAt
        + ", updatedAt=" + updatedAt + "]";
  }

}
