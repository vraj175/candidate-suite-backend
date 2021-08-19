package com.aspire.kgp.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.google.gson.annotations.SerializedName;

@JsonFilter("candidateFilter")
public class CandidateDTO {
	private String id;
	private UserDTO contact;
	private SearchDTO search;
	@SerializedName("kgp_interview_date1")
	private String kgpInterviewDate1;

	@SerializedName("kgp_interview_date2")
	private String kgpInterviewDate2;

	@SerializedName("kgp_interview_date3")
	private String kgpInterviewDate3;
	private List<InterviewDTO> interviews;

	public String getKgpInterviewDate1() {
		return kgpInterviewDate1;
	}

	public void setKgpInterviewDate1(String kgpInterviewDate1) {
		this.kgpInterviewDate1 = kgpInterviewDate1;
	}

	public String getKgpInterviewDate2() {
		return kgpInterviewDate2;
	}

	public void setKgpInterviewDate2(String kgpInterviewDate2) {
		this.kgpInterviewDate2 = kgpInterviewDate2;
	}

	public String getKgpInterviewDate3() {
		return kgpInterviewDate3;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UserDTO getContact() {
		return contact;
	}

	public void setContact(UserDTO contact) {
		this.contact = contact;
	}

	public SearchDTO getSearch() {
		return search;
	}

	public void setSearch(SearchDTO search) {
		this.search = search;
	}
}
