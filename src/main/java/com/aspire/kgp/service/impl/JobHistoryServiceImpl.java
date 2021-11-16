package com.aspire.kgp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aspire.kgp.model.JobHistory;
import com.aspire.kgp.repository.JobHistoryRepository;
import com.aspire.kgp.service.JobHistoryService;

@Service
public class JobHistoryServiceImpl implements JobHistoryService {

  @Autowired
  JobHistoryRepository jobHistoryRepository;

  @Override
  public List<JobHistory> findByGalaxyId(String galaxyId) {
    return jobHistoryRepository.findByGalaxyId(galaxyId);
  }

}
