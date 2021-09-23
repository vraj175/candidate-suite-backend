package com.aspire.kgp.dto;

import java.util.List;

import com.aspire.kgp.util.CommonUtil;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.google.gson.annotations.SerializedName;

@JsonFilter("candidateFilter")
public class CandidateDTO {
  private String id;
  boolean resumeUploaded;
  private ContactDTO contact;
  private SearchDTO search;
  @SerializedName("kgp_interview_date1")
  private String kgpInterviewDate1;
  @SerializedName("kgp_interview_date2")
  private String kgpInterviewDate2;
  @SerializedName("kgp_interview_date3")
  private String kgpInterviewDate3;
  private List<InterviewDTO> interviews;
  boolean degreeVerification;
  boolean offerPresented;
  boolean athenaCompleted;
  @SerializedName("athena_status")
  private String athenaStatus;
  private String contactId;

  public String getContactId() {
    return contactId;
  }

  public void setContactId(String contactId) {
    this.contactId = contactId;
  }

  public String getAthenaStatus() {
    return athenaStatus;
  }

  public void setAthenaStatus(String athenaStatus) {
    this.athenaStatus = athenaStatus;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public boolean isResumeUploaded() {
    return resumeUploaded;
  }

  public void setResumeUploaded(boolean resumeUploaded) {
    this.resumeUploaded = resumeUploaded;
  }

  public boolean isDegreeVerification() {
    return degreeVerification;
  }

  public void setDegreeVerification(boolean degreeVerification) {
    this.degreeVerification = degreeVerification;
  }

  public boolean isOfferPresented() {
    return offerPresented;
  }

  public void setOfferPresented(boolean offerPresented) {
    this.offerPresented = offerPresented;
  }

  public ContactDTO getContact() {
    return contact;
  }

  public void setContact(ContactDTO contact) {
    this.contact = contact;
  }

  public SearchDTO getSearch() {
    return search;
  }

  public void setSearch(SearchDTO search) {
    this.search = search;
  }

  public String getKgpInterviewDate1() {
    return CommonUtil.removeTime(kgpInterviewDate1);
  }

  public void setKgpInterviewDate1(String kgpInterviewDate1) {
    this.kgpInterviewDate1 = kgpInterviewDate1;
  }

  public String getKgpInterviewDate2() {
    return CommonUtil.removeTime(kgpInterviewDate2);
  }

  public void setKgpInterviewDate2(String kgpInterviewDate2) {
    this.kgpInterviewDate2 = kgpInterviewDate2;
  }

  public String getKgpInterviewDate3() {
    return CommonUtil.removeTime(kgpInterviewDate3);
  }

  public void setKgpInterviewDate3(String kgpInterviewDate3) {
    this.kgpInterviewDate3 = kgpInterviewDate3;
  }

  public List<InterviewDTO> getInterviews() {
    return interviews;
  }

  public void setInterviews(List<InterviewDTO> interviews) {
    this.interviews = interviews;
  }

  public boolean isAthenaCompleted() {
    return athenaCompleted;
  }

  public void setAthenaCompleted(boolean athenaCompleted) {
    this.athenaCompleted = athenaCompleted;
  }

}
