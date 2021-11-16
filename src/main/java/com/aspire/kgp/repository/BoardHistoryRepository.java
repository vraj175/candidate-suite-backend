package com.aspire.kgp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aspire.kgp.model.BoardHistory;

@Repository
public interface BoardHistoryRepository extends JpaRepository<BoardHistory, Long> {
  List<BoardHistory> findByGalaxyId(String galaxyId);
  
}
