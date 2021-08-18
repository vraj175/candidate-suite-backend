package com.aspire.kgp.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Component;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.CandidateDTO;
import com.aspire.kgp.dto.SearchDTO;
import com.aspire.kgp.dto.UserDTO;
import com.aspire.kgp.exception.APIException;
import com.aspire.kgp.exception.NotFoundException;
import com.aspire.kgp.model.User;
import com.aspire.kgp.model.UserSearch;
import com.aspire.kgp.service.UserSearchService;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

@Component
public class SearchUtil {
  private static final Log log = LogFactory.getLog(SearchUtil.class);

  @Autowired
  RestUtil restUtil;

  @Autowired
  UserSearchService searchService;

  public final List<SearchDTO> getSearchList(String companyId, String stage) {

    String searchListResponse =
        restUtil.newGetMethod(Constant.SEARCHES_LIST_BY_COMAPNY.replace("{companyId}", companyId));

    return getSearchListFromJsonResponse(searchListResponse, stage);
  }

  public final List<SearchDTO> getSearchListForUser(User user, String stage) {
    List<UserSearch> searches = searchService.findByUser(user);

    if (searches.isEmpty()) {
      return Collections.emptyList();
    }

    JsonArray jsonArray = new JsonArray();
    searches.forEach(e -> jsonArray.add(e.getSearchId()));

    JsonArray attributes = new JsonArray();
    attributes.add("id");
    attributes.add("job_title");
    attributes.add("job_number");
    attributes.add("stage");

    JsonArray attributesCompany = new JsonArray();
    attributesCompany.add("id");
    attributesCompany.add("name");

    JsonObject company = new JsonObject();
    company.addProperty("model", "company");
    company.add("attributes", attributesCompany);

    JsonArray query = new JsonArray();
    query.add(company);

    JsonObject paramJSON = new JsonObject();
    paramJSON.add("ids", jsonArray);
    paramJSON.add("attributes", attributes);
    paramJSON.add("query", query);

    String searchListResponse =
        restUtil.postMethod(Constant.SEARCHES_LIST_BY_IDS, paramJSON.toString());
    return getSearchListFromJsonResponse(searchListResponse, stage);
  }

  private List<SearchDTO> getSearchListFromJsonResponse(String searchListResponse, String stage) {
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

  public MappingJacksonValue applySearchFilter(List<SearchDTO> searchDTOs) {
    SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "jobTitle",
        "jobNumber", "stage", "company");

    FilterProvider filters = new SimpleFilterProvider().addFilter("searchFilter", filter);
    MappingJacksonValue mapping = new MappingJacksonValue(searchDTOs);
    mapping.setFilters(filters);
    return mapping;
  }

  public List<CandidateDTO> getCandidateList(String searchId) {

    UserDTO contact = null;
    CandidateDTO candidate;
    List<CandidateDTO> listCandidate = new ArrayList<>();

    String apiResponse =
        restUtil.newGetMethod(Constant.CANDIDATE_LIST_URL.replace("{searchId}", searchId));
    if (CommonUtil.checkNullString(apiResponse)) {
      log.error("Error while fetching candidate list from Galaxy.");
      return listCandidate;
    }

    JsonObject jsonObjects = (JsonObject) JsonParser.parseString(apiResponse);
    JsonArray jsonArray = jsonObjects.getAsJsonArray("data");

    if (jsonArray == null) {
      throw new NotFoundException("Invalid Search Id");
    }
    Gson gson = new Gson();
    for (JsonElement jsonElement : jsonArray) {
      contact =
          gson.fromJson(jsonElement.getAsJsonObject().get("contact"), new TypeToken<UserDTO>() {
            /**
            *
            */
            private static final long serialVersionUID = 1L;
          }.getType());
      candidate = new CandidateDTO();
      candidate.setId(jsonElement.getAsJsonObject().get("id").getAsString());
      candidate.setContact(contact);
      listCandidate.add(candidate);
    }
    return listCandidate;
  }
}
