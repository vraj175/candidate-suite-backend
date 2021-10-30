package com.aspire.kgp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aspire.kgp.model.JobHistory;

@Repository
public interface JobHistoryRepository extends JpaRepository<JobHistory, Long> {

}
