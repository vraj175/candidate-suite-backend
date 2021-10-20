package com.aspire.kgp.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.CandidateDTO;
import com.aspire.kgp.dto.PositionProfileDTO;
import com.aspire.kgp.dto.SearchDTO;
import com.aspire.kgp.exception.APIException;
import com.aspire.kgp.exception.NotFoundException;
import com.aspire.kgp.model.User;
import com.aspire.kgp.model.UserSearch;
import com.aspire.kgp.service.SearchService;
import com.aspire.kgp.service.UserSearchService;
import com.aspire.kgp.util.RestUtil;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

@Service
public class SearchServiceImpl implements SearchService {

  @Autowired
  RestUtil restUtil;

  @Autowired
  UserSearchService userSearchService;

  @Override
  public final List<SearchDTO> getSearchListForUser(User user, String stage) {
    List<UserSearch> searches = userSearchService.findByUser(user);

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
        restUtil.postMethod(Constant.SEARCHES_LIST_BY_IDS, paramJSON.toString(), null);
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
      throw new APIException(Constant.CONVERT_JSON_ERROR);
    }
    return searchList.stream().filter(s -> stage.equalsIgnoreCase(s.getStage()))
        .collect(Collectors.toList());
  }

  @Override
  public List<SearchDTO> getSearchList(String companyId, String stage) {
    List<UserSearch> searches = userSearchService.findByIsDeletedFalse();
    if (searches.isEmpty()) {
      return Collections.emptyList();
    }
    String searchListResponse =
        restUtil.newGetMethod(Constant.SEARCHES_LIST_BY_COMAPNY.replace("{companyId}", companyId));

    List<SearchDTO> searchDTOs = getSearchListFromJsonResponse(searchListResponse, stage);
    return createSharedListViaStream(searchDTOs, searches);
  }

  public List<SearchDTO> createSharedListViaStream(List<SearchDTO> listOne,
      List<UserSearch> listTwo) {
    // We create a stream of elements from the first list.
    return listOne.stream()
        // We select any elements such that in the stream of elements from the second
        // list
        .filter(two -> listTwo.stream()
            // there is an element that has the same name and school as this element,
            .anyMatch(one -> one.getSearchId().equals(two.getId())))
        // and collect all matching elements from the first list into a new list.
        .collect(Collectors.toList());
    // We return the collected list.
  }

  @Override
  public List<CandidateDTO> getCandidateList(String searchId) {
    List<UserSearch> searches = userSearchService.findByIsDeletedFalse();
    if (searches.isEmpty()) {
      return Collections.emptyList();
    }

    String apiResponse =
        restUtil.newGetMethod(Constant.CANDIDATE_LIST_URL.replace("{searchId}", searchId));

    JsonObject jsonObjects = (JsonObject) JsonParser.parseString(apiResponse);
    JsonArray jsonArray = jsonObjects.getAsJsonArray("data");

    if (jsonArray == null) {
      throw new NotFoundException("Invalid Search Id");
    }
    List<CandidateDTO> candidateList = getCandidateListFromJsonResponse(jsonArray);
    return getInvitedCandidatesFromAllCandidates(candidateList, searches);
  }

  private List<CandidateDTO> getCandidateListFromJsonResponse(JsonArray jsonArray) {
    List<CandidateDTO> candidateList;
    try {
      candidateList = new Gson().fromJson(jsonArray, new TypeToken<List<CandidateDTO>>() {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
      }.getType());
    } catch (JsonSyntaxException e) {
      throw new APIException(Constant.CONVERT_JSON_ERROR);
    }
    return candidateList;
  }

  public List<CandidateDTO> getInvitedCandidatesFromAllCandidates(List<CandidateDTO> allCandidates,
      List<UserSearch> invitedCandidates) {
    // We create a stream of elements from the first list.
    return allCandidates.stream()
        // We select any elements such that in the stream of elements from the second
        // list
        .filter(two -> invitedCandidates.stream()
            // there is an element that has the same name and school as this element,
            .anyMatch(one -> one.getCandidateId().equals(two.getId())))
        // and collect all matching elements from the first list into a new list.
        .collect(Collectors.toList());
    // We return the collected list.
  }

  @Override
  public PositionProfileDTO getPositionProfileDetails(String searchId) {
    PositionProfileDTO positionProfile;
    String apiResponse =
        restUtil.newGetMethod(Constant.POSITION_PROFILE_URL.replace("{searchId}", searchId));
    try {
      JsonObject json = (JsonObject) JsonParser.parseString(apiResponse);
      positionProfile = new Gson().fromJson(json, new TypeToken<PositionProfileDTO>() {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;
      }.getType());
    } catch (Exception e) {
      throw new APIException(Constant.CONVERT_JSON_ERROR);
    }

    if (positionProfile == null || positionProfile.getIsYearsOfExperienceMandatory() == null) {
      throw new APIException("Invalid searchId Id");
    }
    return positionProfile;
  }

  @Override
  public SearchDTO getsearchDetails(String searchId) {
    String apiResponse =
        restUtil.newGetMethod(Constant.SEARCH_INFO_URL.replace("{SEARCHID}", searchId));

    try {
      return new Gson().fromJson(apiResponse, new TypeToken<SearchDTO>() {

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
