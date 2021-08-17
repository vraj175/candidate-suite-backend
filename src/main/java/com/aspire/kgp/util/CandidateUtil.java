package com.aspire.kgp.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.CandidateDTO;
import com.aspire.kgp.dto.UserDTO;
import com.aspire.kgp.exception.APIException;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
public class CandidateUtil {
  static Log log = LogFactory.getLog(CandidateUtil.class.getName());

  @Autowired
  RestUtil restUtil;
  
  public final CandidateDTO getCandidateDetails(String candidateId) {
    String apiResponse = restUtil
        .newGetMethod(Constant.CONDIDATE_URL.replace("{candidateId}", candidateId));
    JsonObject json = (JsonObject) JsonParser.parseString(apiResponse);
    CandidateDTO candidateDTO =
        new Gson().fromJson(json.get("candidate"), new TypeToken<CandidateDTO>() {

          /**
           * 
           */
          private static final long serialVersionUID = 1L;
        }.getType());
    if (candidateDTO == null) {
      throw new APIException("Invalid Candidate Id");
    }
    candidateDTO.getSearch().setPartners(addJsonArraytoList(json, "partners"));
    candidateDTO.getSearch().setRecruiters(addJsonArraytoList(json, "recruiters"));
    candidateDTO.getSearch().setResearchers(addJsonArraytoList(json, "researchers"));
    candidateDTO.getSearch().setEas(addJsonArraytoList(json, "eas"));
    
    return candidateDTO;
  }
  
  private List<UserDTO> addJsonArraytoList(JsonObject json, String listfor){
    JsonArray partnerArray = json.getAsJsonObject("candidate").getAsJsonObject("search").getAsJsonArray(listfor);
    List<UserDTO> partnerList= new ArrayList<>();
    partnerArray.forEach(e -> partnerList.add( new Gson().fromJson(e.getAsJsonObject().get("user"), new TypeToken<UserDTO>() {

      /**
       * 
       */
      private static final long serialVersionUID = 1L;
    }.getType())));
    return partnerList;
  }
  
}
