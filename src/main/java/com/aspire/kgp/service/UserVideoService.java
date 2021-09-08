package com.aspire.kgp.service;

import java.util.List;

import com.aspire.kgp.model.UserVideo;

public interface UserVideoService {
  UserVideo saveorUpdate(UserVideo userVideo);

  UserVideo addCandidateVideo(String candidateId, String fileToken);
  
  List<UserVideo> findByCandidateId(String candidateId);
}
