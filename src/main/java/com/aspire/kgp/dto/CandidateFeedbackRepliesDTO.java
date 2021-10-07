package com.aspire.kgp.dto;

import com.google.gson.annotations.SerializedName;

public class CandidateFeedbackRepliesDTO extends CandidateFeedbackBaseDTO {

  @SerializedName("comment_id")
  private String commentId;
  private String reply;

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

  @Override
  public String toString() {
    return "CandidateFeedbackRepliesDTO [commentId=" + commentId + ", reply=" + reply + ", getId()="
        + getId() + ", getCandidateId()=" + getCandidateId() + ", getCreatedBy()=" + getCreatedBy()
        + ", getCreatedAt()=" + getCreatedAt() + ", getUpdatedAt()=" + getUpdatedAt() + "]";
  }
}
