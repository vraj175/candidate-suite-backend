package com.aspire.kgp.service;

import java.util.List;

import com.aspire.kgp.model.JobHistory;

public interface JobHistoryService {
  public List<JobHistory> findByGalaxyId(String galaxyId);
}
