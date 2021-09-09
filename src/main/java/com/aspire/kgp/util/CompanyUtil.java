package com.aspire.kgp.util;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.CandidateDTO;
import com.aspire.kgp.dto.CompanyDTO;
import com.aspire.kgp.dto.ContactDTO;
import com.aspire.kgp.dto.InterviewDTO;
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

    String companyListResponse =
        restUtil.newGetMethod(Constant.COMPANY_LIST.replace("{STAGE}", stage));
    try {
      return new Gson().fromJson(companyListResponse, new TypeToken<List<CompanyDTO>>() {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
      }.getType());
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

  public CandidateDTO getCompanyInfoDetails(String candidateId) throws ParseException {
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
      convertDateFormat(candidateDTO);
    }
    if (candidateContactDTO.getAthenaStatus() != null
        && candidateContactDTO.getAthenaStatus().equalsIgnoreCase(Constant.COMPLETED)
        && candidateDTO != null) {
      candidateDTO.setContactId(candidateContactDTO.getId());
      candidateDTO.setAthenaCompleted(Boolean.TRUE);
    }

    return candidateDTO;
  }

  private CandidateDTO convertDateFormat(CandidateDTO candidateDTO) throws ParseException {
    if (candidateDTO.getKgpInterviewDate1() != null
        && !candidateDTO.getKgpInterviewDate1().isEmpty())
      candidateDTO
          .setKgpInterviewDate1(CommonUtil.dateFormatter(candidateDTO.getKgpInterviewDate1()));
    if (candidateDTO.getKgpInterviewDate2() != null
        && !candidateDTO.getKgpInterviewDate2().isEmpty())
      candidateDTO
          .setKgpInterviewDate2(CommonUtil.dateFormatter(candidateDTO.getKgpInterviewDate2()));
    if (candidateDTO.getKgpInterviewDate3() != null
        && !candidateDTO.getKgpInterviewDate3().isEmpty())
      candidateDTO
          .setKgpInterviewDate3(CommonUtil.dateFormatter(candidateDTO.getKgpInterviewDate3()));
    if (candidateDTO.getInterviews() != null && !candidateDTO.getInterviews().isEmpty())
      for (InterviewDTO interviewDetails : candidateDTO.getInterviews()) {
        if (interviewDetails.getInterviewDate() != null
            && !interviewDetails.getInterviewDate().isEmpty())
          interviewDetails
              .setInterviewDate(CommonUtil.dateFormatter(interviewDetails.getInterviewDate()));
      }
    return candidateDTO;
  }
}
