package com.aspire.kgp.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("candidateFeedbackFilter")
public class CandidateFeedbackDTO extends CandidateFeedbackReplyDTO {

  private String comments;
  private boolean status;
  private List<CandidateFeedbackReplyDTO> replies;

  public boolean isStatus() {
    return status;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public List<CandidateFeedbackReplyDTO> getReplies() {
    return replies;
  }

  public void setReplies(List<CandidateFeedbackReplyDTO> replies) {
    this.replies = replies;
  }

  @Override
  public String toString() {
    return "CandidateFeedbackDTO [comments=" + comments + ", replies=" + replies + ", getId()="
        + getId() + ", getCandidateId()=" + getCandidateId() + ", getCreatedBy()=" + getCreatedBy()
        + ", getCreatedAt()=" + getCreatedAt() + ", getUpdatedAt()=" + getUpdatedAt()
        + ", getCommentId()=" + getCommentId() + ", getReply()=" + getReply() + "]";
  }
}
