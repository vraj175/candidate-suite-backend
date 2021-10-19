package com.aspire.kgp.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aspire.kgp.model.UserSearch;
import com.aspire.kgp.model.UserVideo;
import com.aspire.kgp.repository.UserVideoRepository;
import com.aspire.kgp.service.UserSearchService;
import com.aspire.kgp.service.UserVideoService;

@Service
public class UserVideoServiceImpl implements UserVideoService {
  static final Log log = LogFactory.getLog(UserVideoServiceImpl.class);

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
    UserSearch userSearch = new UserSearch();
    userSearch.setId(1);
    userVideo.setUserSearch(userSearch);
    userVideo.setContactId(contactId);
    userVideo.setFileToken(fileToken);
    return saveorUpdate(userVideo);
  }

  @Override
  public List<UserVideo> findByContactId(String contactId) {
    return repository.findByContactIdAndIsDeletedFalseOrderByCreatedDateDesc(contactId);
  }

}
