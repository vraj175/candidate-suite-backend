package com.aspire.kgp.dto;

import java.util.List;

public class CandidateFeedbackDTO extends CandidateFeedbackBaseDTO {

  private String comments;
  private List<CandidateFeedbackRepliesDTO> replies;

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public List<CandidateFeedbackRepliesDTO> getReplies() {
    return replies;
  }

  public void setReplies(List<CandidateFeedbackRepliesDTO> replies) {
    this.replies = replies;
  }

  @Override
  public String toString() {
    return "CandidateFeedbackDTO [comments=" + comments + ", replies=" + replies + ", getId()="
        + getId() + ", getCandidateId()=" + getCandidateId() + ", getCreatedBy()=" + getCreatedBy()
        + ", getCreatedAt()=" + getCreatedAt() + ", getUpdatedAt()=" + getUpdatedAt() + "]";
  }
}
