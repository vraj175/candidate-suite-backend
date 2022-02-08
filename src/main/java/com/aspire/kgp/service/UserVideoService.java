package com.aspire.kgp.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.aspire.kgp.model.UserVideo;

public interface UserVideoService {
  UserVideo saveorUpdate(UserVideo userVideo);

  UserVideo addContactVideo(String contactId, String fileToken, String candidateId, HttpServletRequest request);

  List<UserVideo> findByContactId(String contactId);

  int getS3BucketVideoStatusCode(String videoToken);
}
