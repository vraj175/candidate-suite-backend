package com.aspire.kgp.dto;

import com.google.gson.annotations.SerializedName;

public class CompanySearchDTO {

	@SerializedName("current_job_title")
	private String jobTitle;
	@SerializedName("name")
	private String comapnyName;
	@SerializedName("country")
	private String location;

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getComapnyName() {
		return comapnyName;
	}

	public void setComapnyName(String comapnyName) {
		this.comapnyName = comapnyName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
