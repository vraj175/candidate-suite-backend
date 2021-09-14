package com.aspire.kgp.util;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.CandidateDTO;
import com.aspire.kgp.dto.CompanyDTO;
import com.aspire.kgp.dto.ContactDTO;
import com.aspire.kgp.dto.InterviewDTO;
import com.aspire.kgp.exception.APIException;
import com.aspire.kgp.model.UserSearch;
import com.aspire.kgp.service.UserSearchService;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

@Component
public class CompanyUtil {

  @Autowired
  RestUtil restUtil;
  
  @Autowired
  UserSearchService searchService;

  public final List<CompanyDTO> getCompanyList(String stage) {
    List<UserSearch> invitedCompanies = searchService.findByIsDeletedFalse();
    if (invitedCompanies.isEmpty()) {
      return Collections.emptyList();
    }

    String companyListResponse =
        restUtil.newGetMethod(Constant.COMPANY_LIST.replace("{STAGE}", stage));
    try {
      List<CompanyDTO> allCompanies = new Gson().fromJson(companyListResponse, new TypeToken<List<CompanyDTO>>() {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
      }.getType());
      return getInvitedCompaniesFromAllCompanies(allCompanies, invitedCompanies);
    } catch (JsonSyntaxException e) {
      throw new APIException(Constant.JSON_PROCESSING_EXCEPTION + e.getMessage());
    }
    
    
  }

  public ContactDTO getCompanyDetails(String candidateId) {
    String apiResponse =
        restUtil.newGetMethod(Constant.CANDIDATE_URL.replace("{candidateId}", candidateId));
    JsonObject json = (JsonObject) JsonParser.parseString(apiResponse);
    ContactDTO contactDTO;
    try {
      contactDTO =
          new Gson().fromJson(json.getAsJsonObject("contact"), new TypeToken<ContactDTO>() {

            /**
             * 
             */
            private static final long serialVersionUID = 1L;
          }.getType());
      if (contactDTO == null) {
        throw new APIException(Constant.INVALID_CANDIDATE_ID);
      }
    } catch (JsonSyntaxException e) {
      throw new APIException(Constant.JSON_PROCESSING_EXCEPTION + e.getMessage());
    }
    return contactDTO;
  }

  public CandidateDTO getCompanyInfoDetails(String candidateId) {
    String apiResponse =
        restUtil.newGetMethod(Constant.CANDIDATE_URL.replace("{candidateId}", candidateId));
    JsonObject json = (JsonObject) JsonParser.parseString(apiResponse);
    JsonObject contact = json.getAsJsonObject("contact");
    JsonObject jsonObjects = json.getAsJsonObject("candidate");
    CandidateDTO candidateDTO;
    CandidateDTO candidateContactDTO;
    try {
      candidateDTO = new Gson().fromJson(jsonObjects, new TypeToken<CandidateDTO>() {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
      }.getType());
      candidateContactDTO = new Gson().fromJson(contact, new TypeToken<CandidateDTO>() {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
      }.getType());

      if (candidateDTO == null && candidateContactDTO == null) {
        throw new APIException(Constant.INVALID_CANDIDATE_ID);
      }
    } catch (JsonSyntaxException e) {
      throw new APIException(Constant.JSON_PROCESSING_EXCEPTION + e.getMessage());
    }
    if (candidateDTO != null) {
      candidateDTO.setDegreeVerification(Boolean.TRUE);
      candidateDTO.setOfferPresented(Boolean.TRUE);
      mergeName(candidateDTO);
    }
    if (candidateContactDTO.getAthenaStatus() != null
        && candidateContactDTO.getAthenaStatus().equalsIgnoreCase(Constant.COMPLETED)
        && candidateDTO != null) {
      candidateDTO.setContactId(candidateContactDTO.getId());
      candidateDTO.setAthenaCompleted(Boolean.TRUE);
    }

    return candidateDTO;
  }

  private CandidateDTO mergeName(CandidateDTO candidateDTO) {
    if (candidateDTO.getInterviews() != null && !candidateDTO.getInterviews().isEmpty())
      for (InterviewDTO interviewDetails : candidateDTO.getInterviews()) {
        if (interviewDetails.getClient() != null)
          interviewDetails.getClient().setName(interviewDetails.getClient().getFirstName() + " "
              + interviewDetails.getClient().getLastName());
      }
    return candidateDTO;
  }

  public List<CompanyDTO> getListOfCompany(String companyName) {
    String apiResponse =
        restUtil.newGetMethod(Constant.GET_COMPANY_LIST_URL.replace("{COMPANYNAME}", companyName));
    try {
      return new Gson().fromJson(apiResponse, new TypeToken<List<CompanyDTO>>() {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
      }.getType());
    } catch (JsonSyntaxException e) {
      throw new APIException(Constant.JSON_PROCESSING_EXCEPTION + e.getMessage());
    }
  }
  
  public List<CompanyDTO> getInvitedCompaniesFromAllCompanies(List<CompanyDTO> allCompanies,
      List<UserSearch> invitedCompanies) {
    // We create a stream of elements from the first list.
    return allCompanies.stream()
        // We select any elements such that in the stream of elements from the second
        // list
        .filter(two -> invitedCompanies.stream()
            // there is an element that has the same name and school as this element,
            .anyMatch(one -> one.getCompanyId().equals(two.getId())))
        // and collect all matching elements from the first list into a new list.
        .collect(Collectors.toList());
    // We return the collected list.
  }
}
