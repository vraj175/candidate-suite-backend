package com.aspire.kgp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aspire.kgp.model.BoardHistory;
import com.aspire.kgp.repository.BoardHistoryRepository;
import com.aspire.kgp.service.BoardHistoryService;

@Service
public class BoardHistoryServiceImpl implements BoardHistoryService {

  @Autowired
  BoardHistoryRepository boardHistoryRepository;

  @Override
  public List<BoardHistory> findByGalaxyId(String galaxyId) {
    return boardHistoryRepository.findByGalaxyId(galaxyId);
  }

}
