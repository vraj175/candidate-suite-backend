package com.aspire.kgp.util;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.SearchDTO;
import com.aspire.kgp.exception.APIException;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

@Component
public class SearchUtil {

  @Autowired
  RestUtil restUtil;

  public final List<SearchDTO> getSearchList(String companyId, String stage) {

    String searchListResponse =
        restUtil.newGetMethod(Constant.SEARCHES_LIST_BY_COMAPNY.replace("{companyId}", companyId));

    List<SearchDTO> searchList;
    try {
      searchList = new Gson().fromJson(searchListResponse, new TypeToken<List<SearchDTO>>() {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
      }.getType());
    } catch (JsonSyntaxException e) {
      throw new APIException("Error in coverting json to object");
    }
    return searchList.stream().filter(s -> stage.equalsIgnoreCase(s.getStage()))
        .collect(Collectors.toList());
  }
}
