package com.aspire.kgp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aspire.kgp.model.BoardHistory;

public interface BoardHistoryRepository extends JpaRepository<BoardHistory, Long> {

}
