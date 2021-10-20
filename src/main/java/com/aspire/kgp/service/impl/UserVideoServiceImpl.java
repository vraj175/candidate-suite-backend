package com.aspire.kgp.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.model.UserVideo;
import com.aspire.kgp.repository.UserVideoRepository;
import com.aspire.kgp.service.UserSearchService;
import com.aspire.kgp.service.UserVideoService;
import com.aspire.kgp.util.RestUtil;

@Service
public class UserVideoServiceImpl implements UserVideoService {
  static final Log log = LogFactory.getLog(UserVideoServiceImpl.class);

  @Value("${s3Bucket.url}")
  private String s3BucketbaseApiUrl;

  @Autowired
  RestUtil restUtil;

  @Autowired
  UserVideoRepository repository;

  @Autowired
  UserSearchService searchService;

  @Override
  public UserVideo saveorUpdate(UserVideo userVideo) {
    return repository.save(userVideo);
  }

  @Override
  public UserVideo addContactVideo(String contactId, String fileToken) {
    UserVideo userVideo = new UserVideo();
    userVideo.setContactId(contactId);
    userVideo.setFileToken(fileToken);
    return saveorUpdate(userVideo);
  }

  @Override
  public List<UserVideo> findByContactId(String contactId) {
    return repository.findByContactIdAndIsDeletedFalseOrderByCreatedDateDesc(contactId);
  }

  @Override
  public int getS3BucketVideoStatusCode(String videoToken) {
    GetMethod get = new GetMethod(
        s3BucketbaseApiUrl + "/" + videoToken.replaceAll(Constant.SPACE_STRING, "%20"));
    try {
      log.info("Request time: " + new Date());
      new HttpClient().executeMethod(get);
      log.info(get.getStatusCode());
      return get.getStatusCode();

    } catch (IOException e) {
      log.error("error " + e);
    } finally {
      get.releaseConnection();
    }
    return 0;
  }
}
