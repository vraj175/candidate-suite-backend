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
  private String stage;
  @SerializedName("kgp_interview_method1")
  private String kgpInterviewMethod1;
  @SerializedName("kgp_interview_method2")
  private String kgpInterviewMethod2;
  @SerializedName("kgp_interview_method3")
  private String kgpInterviewMethod3;
  @SerializedName("kgp_interview_client1")
  private UserDTO kgpInterviewClient1;
  @SerializedName("kgp_interview_client2")
  private UserDTO kgpInterviewClient2;
  @SerializedName("kgp_interview_client3")
  private UserDTO kgpInterviewClient3;
  @SerializedName("screened_date")
  private String screenedDate;

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

  public String getStage() {
    return stage;
  }

  public void setStage(String stage) {
    this.stage = stage;
  }

  public String getKgpInterviewMethod1() {
    return kgpInterviewMethod1;
  }

  public void setKgpInterviewMethod1(String kgpInterviewMethod1) {
    this.kgpInterviewMethod1 = kgpInterviewMethod1;
  }

  public String getKgpInterviewMethod2() {
    return kgpInterviewMethod2;
  }

  public void setKgpInterviewMethod2(String kgpInterviewMethod2) {
    this.kgpInterviewMethod2 = kgpInterviewMethod2;
  }

  public String getKgpInterviewMethod3() {
    return kgpInterviewMethod3;
  }

  public void setKgpInterviewMethod3(String kgpInterviewMethod3) {
    this.kgpInterviewMethod3 = kgpInterviewMethod3;
  }

  public UserDTO getKgpInterviewClient1() {
    return kgpInterviewClient1;
  }

  public void setKgpInterviewClient1(UserDTO kgpInterviewClient1) {
    this.kgpInterviewClient1 = kgpInterviewClient1;
  }

  public UserDTO getKgpInterviewClient2() {
    return kgpInterviewClient2;
  }

  public void setKgpInterviewClient2(UserDTO kgpInterviewClient2) {
    this.kgpInterviewClient2 = kgpInterviewClient2;
  }

  public UserDTO getKgpInterviewClient3() {
    return kgpInterviewClient3;
  }

  public void setKgpInterviewClient3(UserDTO kgpInterviewClient3) {
    this.kgpInterviewClient3 = kgpInterviewClient3;
  }

  public String getScreenedDate() {
    return CommonUtil.removeTime(screenedDate);
  }

  public void setScreenedDate(String screenedDate) {
    this.screenedDate = screenedDate;
  }

  @Override
  public String toString() {
    return "CandidateDTO [id=" + id + ", resumeUploaded=" + resumeUploaded + ", contact=" + contact
        + ", search=" + search + ", kgpInterviewDate1=" + kgpInterviewDate1 + ", kgpInterviewDate2="
        + kgpInterviewDate2 + ", kgpInterviewDate3=" + kgpInterviewDate3 + ", interviews="
        + interviews + ", degreeVerification=" + degreeVerification + ", offerPresented="
        + offerPresented + ", athenaCompleted=" + athenaCompleted + ", athenaStatus=" + athenaStatus
        + ", contactId=" + contactId + "]";
  }

}
