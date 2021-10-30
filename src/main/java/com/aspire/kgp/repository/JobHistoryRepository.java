package com.aspire.kgp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aspire.kgp.model.JobHistory;

public interface JobHistoryRepository extends JpaRepository<JobHistory, Long> {

}
