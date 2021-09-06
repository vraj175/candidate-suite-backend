package com.aspire.kgp.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aspire.kgp.exception.NotFoundException;
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
  public UserVideo addCandidateVideo(String candidateId, String fileToken) {
    UserSearch userSearch = searchService.findByCandidateId(candidateId);
    if (userSearch == null) {
      throw new NotFoundException("Candidate is not available");
    }
    UserVideo userVideo = new UserVideo();
    userVideo.setUserSearch(userSearch);
    userVideo.setFileToken(fileToken);
    return saveorUpdate(userVideo);
  }

}
