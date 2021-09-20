package com.aspire.kgp.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.PickListDTO;
import com.aspire.kgp.exception.APIException;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

@Component
public class PickListUtil {

  @Autowired
  RestUtil restUtil;

  public List<String> getEducationDegrees() {
    String apiResponse = restUtil.newGetMethod(Constant.EDUCATION_DEGREE_PICKLIST_URL);
    try {
      return new Gson().fromJson(apiResponse, new TypeToken<List<String>>() {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
      }.getType());
    } catch (JsonSyntaxException e) {
      throw new APIException(Constant.JSON_PROCESSING_EXCEPTION + e.getMessage());
    }
  }

  public List<PickListDTO> getReferencesType() {
    String apiResponse = restUtil.newGetMethod(Constant.REFERENCE_TYPE_PICKLIST_URL);
    try {
      return new Gson().fromJson(apiResponse, new TypeToken<List<PickListDTO>>() {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
      }.getType());
    } catch (JsonSyntaxException e) {
      throw new APIException(Constant.JSON_PROCESSING_EXCEPTION + e.getMessage());
    }
  }
}
