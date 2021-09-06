package com.aspire.kgp.service;

import com.aspire.kgp.model.UserVideo;

public interface UserVideoService {
  UserVideo saveorUpdate(UserVideo userVideo);

  UserVideo addCandidateVideo(String candidateId, String fileToken);
}
