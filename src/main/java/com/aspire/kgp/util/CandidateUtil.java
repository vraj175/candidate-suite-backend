package com.aspire.kgp.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.CandidateDTO;
import com.aspire.kgp.dto.PartnerDTO;
import com.aspire.kgp.dto.RecruiterDTO;
import com.aspire.kgp.dto.UserDTO;
import com.aspire.kgp.dto.WelcomeResponseDTO;
import com.aspire.kgp.exception.NotFoundException;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
public class CandidateUtil {
  static Log log = LogFactory.getLog(CandidateUtil.class.getName());

  @Autowired
  RestUtil restUtil;

  /*
   * Used In welcome Page response. Take response from galaxy(get candidate details using
   * candidate_Id) & prepared it for welcome page
   */
  public WelcomeResponseDTO getWelcomePageDetails(String candidateId) {
    WelcomeResponseDTO welcomeResponseDTO = new WelcomeResponseDTO();

    CandidateDTO candidateDTO = getCandidateDetails(candidateId);

    try {
      welcomeResponseDTO.setCandidateName(
          candidateDTO.getContact().getFirstName() + " " + candidateDTO.getContact().getLastName());
      welcomeResponseDTO.setJobTitle(candidateDTO.getSearch().getJobTitle());
      welcomeResponseDTO.setCompanyName(candidateDTO.getSearch().getCompany().getName());

      List<PartnerDTO> partnerList = new ArrayList<>();
      for (PartnerDTO partnerRes : candidateDTO.getSearch().getPartners()) {
        partnerList.add(fetchPartnerFromUser(partnerRes.getUser()));
      }
      for (RecruiterDTO recruiterRes : candidateDTO.getSearch().getRecruiters()) {
        partnerList.add(fetchPartnerFromUser(recruiterRes.getUser()));
      }
      welcomeResponseDTO.setPartners(partnerList);
      return welcomeResponseDTO;
    } catch (Exception e) {
      log.error("Error while welcome page response." + e.getMessage());
      return welcomeResponseDTO;
    }

  }

  /*
   * Galaxy API call and get response of candidate details along associated search details
   */
  public CandidateDTO getCandidateDetails(String candidateId) {
    CandidateDTO candidateDTO = null;
    String apiResponse =
        restUtil.newGetMethod(Constant.CONDIDATE_URL.replace("{candidateId}", candidateId));
    if (CommonUtil.checkNullString(apiResponse)) {
      log.error("Error while fetching candidate Info from Galaxy.");
      return candidateDTO;
    }
    try {
      JsonObject json = (JsonObject) JsonParser.parseString(apiResponse);
      candidateDTO = new Gson().fromJson(json.get("candidate"), new TypeToken<CandidateDTO>() {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;
      }.getType());
    } catch (Exception e) {
      log.error("Invalid Json for candidate Info");
      return candidateDTO;
    }

    if (candidateDTO == null) {
      throw new NotFoundException("Invalid Candidate Id");
    }
    return candidateDTO;
  }

  public PartnerDTO fetchPartnerFromUser(UserDTO user) {
    PartnerDTO partner = new PartnerDTO();
    try {
      partner.setPartnerName(user.getName());
      // partner.setUrl(synclinkUrl
      // + Constant.PARTNER_IMAGE_URL.replace("{partnerName}", user.getName().replace(" ", "_"))
      // .replace("{partnerId}", user.getId()));
      partner.setUrl(
          "https://synclink.kingsleygate.com/documents/46500/94803/Michael_Seeley/fc9e8e62-799f-4a03-a58b-154c905735c5");
      partner
          .setPartnerTitle(CommonUtil.checkNotNullString(user.getTitle()) ? user.getTitle() : "");
    } catch (Exception e) {
      log.error("Error while fetching partner object from user.");
    }
    return partner;
  }
}
