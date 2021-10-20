package com.aspire.kgp.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CandidateFeedbackRequestDTO {

  public interface CandidateFeedbackReq {
  }
  public interface CandidateFeedbackReplyReq {
  }
  public interface CandidateFeedbackStatusUpdateReq {
  }

  @JsonProperty(value = "candidateId", required = true)
  @NotEmpty(message = "candidate id cannot be missing or empty",
      groups = {CandidateFeedbackReq.class, CandidateFeedbackReplyReq.class})
  private String candidateId;

  @JsonProperty(value = "comments", required = true)
  @NotEmpty(message = "comments Msg cannot be missing or empty",
      groups = {CandidateFeedbackReq.class})
  private String comments;

  @JsonProperty(value = "commentId", required = true)
  @NotEmpty(message = "comment id cannot be missing or empty",
      groups = {CandidateFeedbackReplyReq.class, CandidateFeedbackStatusUpdateReq.class})
  private String commentId;

  @JsonProperty(value = "reply", required = true)
  @NotEmpty(message = "reply Msg cannot be missing or empty",
      groups = {CandidateFeedbackReplyReq.class})
  private String reply;

  @JsonProperty(value = "status", required = true)
  @NotNull(message = "status cannot be missing or empty",
      groups = {CandidateFeedbackStatusUpdateReq.class})
  private boolean status;

  public boolean isStatus() {
    return status;
  }

  public void setStatus(boolean status) {
    this.status = status;
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
}
