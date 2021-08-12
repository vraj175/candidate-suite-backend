package com.aspire.kgp.util;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.CompanyDTO;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

@Component
public class GalaxyUtil {
  static Log log = LogFactory.getLog(GalaxyUtil.class.getName());
  @Autowired
  RestUtil restUtil;

  public final List<CompanyDTO> getCompanyList(HttpServletRequest request, String stage) {

    String companyListResponse =
        restUtil.newGetMethod(Constant.COMPANY_LIST.replace("{STAGE}", stage));

    List<CompanyDTO> companyList = Collections.emptyList();
    try {
      companyList = new Gson().fromJson(companyListResponse, new TypeToken<List<CompanyDTO>>() {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
      }.getType());
    } catch (JsonSyntaxException e) {
      log.error("oops ! invalid json");
      companyList = Collections.emptyList();
    }
    return companyList;
  }
}
