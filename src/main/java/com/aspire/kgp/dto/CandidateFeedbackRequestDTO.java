package com.aspire.kgp.dto;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CandidateFeedbackRequestDTO {

  public interface CandidateFeedbackReq {
  }
  public interface CandidateFeedbackReplyReq {
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
      groups = {CandidateFeedbackReplyReq.class})
  private String commentId;

  @JsonProperty(value = "reply", required = true)
  @NotEmpty(message = "reply Msg cannot be missing or empty",
      groups = {CandidateFeedbackReplyReq.class})
  private String reply;

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
