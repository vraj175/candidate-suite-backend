package com.aspire.kgp.dto;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.SerializedName;

@JsonFilter("userFilter")
public class UserDTO {
	private String id;
	@SerializedName("first_name")
	private String firstName;
	@SerializedName("last_name")
	private String lastName;
	@SerializedName("work_email")
	private String workEmail;
	@SerializedName(value = "private_email", alternate = { "email" })
	private String email;
	private String role;
	@JsonIgnore
	private String token;
	@SerializedName("current_job_title")
	private String currentJobTitle;
	private CompanyDTO company;
	@SerializedName("country")
	private String location;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCurrentJobTitle() {
		return currentJobTitle;
	}

	public void setCurrentJobTitle(String currentJobTitle) {
		this.currentJobTitle = currentJobTitle;
	}

	public CompanyDTO getCompany() {
		return company;
	}

	public void setCompany(CompanyDTO company) {
		this.company = company;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getWorkEmail() {
		return workEmail;
	}

	public void setWorkEmail(String workEmail) {
		this.workEmail = workEmail;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
