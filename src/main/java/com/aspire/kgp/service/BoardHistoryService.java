package com.aspire.kgp.service;

import java.util.List;

import com.aspire.kgp.model.BoardHistory;

public interface BoardHistoryService {
  public List<BoardHistory> findByGalaxyId(String galaxyId);
}
