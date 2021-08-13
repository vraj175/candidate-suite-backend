package com.aspire.kgp.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.CompanyDTO;
import com.aspire.kgp.exception.APIException;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
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
      throw new APIException("Error in coverting json to object");
    }
  }
}
