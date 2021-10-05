package com.aspire.kgp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CandidateSuiteBackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(CandidateSuiteBackendApplication.class, args);
  }
}
