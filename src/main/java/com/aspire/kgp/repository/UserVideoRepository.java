package com.aspire.kgp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aspire.kgp.model.UserVideo;

@Repository
public interface UserVideoRepository extends JpaRepository<UserVideo, Long> {

}
