package com.aspire.kgp.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.CandidateDTO;
import com.aspire.kgp.dto.CompanyDTO;
import com.aspire.kgp.dto.UserDTO;
import com.aspire.kgp.exception.APIException;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

@Component
public class CompanyUtil {

	@Autowired
	RestUtil restUtil;

	public final List<CompanyDTO> getCompanyList(String stage) {

		String companyListResponse = restUtil.newGetMethod(Constant.COMPANY_LIST.replace("{STAGE}", stage));
		try {
			return new Gson().fromJson(companyListResponse, new TypeToken<List<CompanyDTO>>() {

				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
			}.getType());
		} catch (JsonSyntaxException e) {
			throw new APIException("Error in coverting json to object");
		}
	}

	public UserDTO getCompanyDetails(String candidateId) {
		String apiResponse = restUtil.newGetMethod(Constant.CANDIDATE_URL.replace("{candidateId}", candidateId));
		JsonObject json = (JsonObject) JsonParser.parseString(apiResponse);
		JsonObject jsonObj = json.getAsJsonObject("contact");
		UserDTO userDTO = new UserDTO();
		try {
			userDTO = new Gson().fromJson(jsonObj, new TypeToken<UserDTO>() {

				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
			}.getType());
			if (userDTO == null) {
				throw new APIException("Candidate Id is not valid");
			}
		} catch (JsonSyntaxException e) {
			throw new APIException("Error in coverting json to object");
		}
		return userDTO;
	}

	public CandidateDTO getCompanyInfoDetails(String candidateId) {
		String apiResponse = restUtil.newGetMethod(Constant.CANDIDATE_URL.replace("{candidateId}", candidateId));
		JsonObject json = (JsonObject) JsonParser.parseString(apiResponse);
		JsonObject jsonObjects = json.getAsJsonObject("candidate");
		CandidateDTO candidateDTO = new CandidateDTO();
		try {
			candidateDTO = new Gson().fromJson(jsonObjects, new TypeToken<CandidateDTO>() {

				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
			}.getType());
			if (candidateDTO == null) {
				throw new APIException("Candidate Id is not valid");
			}
		} catch (JsonSyntaxException e) {
			throw new APIException("Error in coverting json to object");
		}
		return candidateDTO;
	}
}
