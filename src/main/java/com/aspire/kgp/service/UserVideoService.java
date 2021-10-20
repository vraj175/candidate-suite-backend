package com.aspire.kgp.service;

import java.util.List;

import com.aspire.kgp.model.UserVideo;

public interface UserVideoService {
  UserVideo saveorUpdate(UserVideo userVideo);

  UserVideo addContactVideo(String candidateId, String fileToken);

  List<UserVideo> findByContactId(String contactId);

  int getS3BucketVideoStatusCode(String videoToken);
}
