package com.aspire.kgp.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.CandidateDTO;
import com.aspire.kgp.dto.PositionProfileDTO;
import com.aspire.kgp.dto.SearchDTO;
import com.aspire.kgp.model.User;
import com.aspire.kgp.util.SearchUtil;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1.0")
@Tag(name = "Search", description = "Rest API For Search")
public class SearchController {

  @Autowired
  SearchUtil searchUtil;

  @Operation(summary = "Get Search list for user")
  @GetMapping("/searches/stage/{stage}")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = "application/json", schema = @Schema(
          type = "List<CandidateDTO>",
          example = "[{\"id\": \"string\",\"search\": {\"id\": \"string\",\"jobTitle\": \"string\",\"jobNumber\": \"string\",\"stage\": \"string\",\"company\": {\"id\": \"string\",\"name\": \"string\"}}}]")))})
  public MappingJacksonValue getCompanyList(HttpServletRequest request,
      @PathVariable("stage") String stage) {
    User user = (User) request.getAttribute("user");
    List<CandidateDTO> candidateList = searchUtil.getSearchListForUser(user, stage);

    SimpleBeanPropertyFilter companyFilter =
        SimpleBeanPropertyFilter.filterOutAllExcept("id", "name");

    SimpleBeanPropertyFilter searchFilter = SimpleBeanPropertyFilter.filterOutAllExcept("id",
        "jobTitle", "jobNumber", "stage", Constant.COMPANY);
    SimpleBeanPropertyFilter candidateFilter =
        SimpleBeanPropertyFilter.filterOutAllExcept("id", "search");

    FilterProvider filters = new SimpleFilterProvider().addFilter("searchFilter", searchFilter)
        .addFilter(Constant.COMPANY_FILTER, companyFilter)
        .addFilter("candidateFilter", candidateFilter);
    MappingJacksonValue mapping = new MappingJacksonValue(candidateList);
    mapping.setFilters(filters);
    return mapping;
  }

  @Operation(summary = "Get Search list for company")
  @GetMapping("/searches/companies/{companyId}/stage/{stage}")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = "application/json", schema = @Schema(type = "List<SearchDTO>",
          example = "[{\"id\": \"string\",\"jobTitle\": \"string\",\"jobNumber\": \"string\",\"stage\": \"string\",\"company\": {\"id\": \"string\",\"name\": \"string\"}}]")))})
  public MappingJacksonValue getCompanyList(@PathVariable("companyId") String companyId,
      @PathVariable("stage") String stage) {
    return searchUtil.applySearchFilter(searchUtil.getSearchList(companyId, stage));
  }

  @Operation(summary = "Get Candidate list")
  @GetMapping(value = {"/searches/{searchId}/candidates"})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = "application/json", schema = @Schema(
          type = "List<CandidateDTO>",
          example = "[{\"id\": \"string\",\"contact\": {\"id\": \"string\",\"firstName\": \"string\",\"lastName\": \"string\",\"workEmail\": \"string\",\"currentJobTitle\": \"string\",\"company\": {\"id\": \"string\",\"name\": \"string\"}}}]")))})
  public MappingJacksonValue getCandidateList(@PathVariable("searchId") String searchId) {
    List<CandidateDTO> listCandidate = searchUtil.getCandidateList(searchId);

    SimpleBeanPropertyFilter companyFilter =
        SimpleBeanPropertyFilter.filterOutAllExcept("id", "name");

    SimpleBeanPropertyFilter contactFilter = SimpleBeanPropertyFilter.filterOutAllExcept("id",
        "firstName", "lastName", "workEmail", "currentJobTitle", Constant.COMPANY);

    SimpleBeanPropertyFilter candidateFilter =
        SimpleBeanPropertyFilter.filterOutAllExcept("id", "contact");

    FilterProvider filters = new SimpleFilterProvider().addFilter("contactFilter", contactFilter)
        .addFilter(Constant.COMPANY_FILTER, companyFilter)
        .addFilter("candidateFilter", candidateFilter);

    MappingJacksonValue mapping = new MappingJacksonValue(listCandidate);
    mapping.setFilters(filters);
    return mapping;
  }

  @Operation(summary = "Get Postion Profile Details")
  @GetMapping(value = {"/searches/{searchId}/position_profile"})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = "application/json", schema = @Schema(
          type = "PositionProfileDTO",
          example = "{\"isDegreeMandatory\": true,\"isApprovedByPartner\": true,\"isYearsOfExperienceMandatory\": true,\"positionOverview\": \"string\",\"productsServicesOverview\": \"string\",\"professionalExperience\": \"string\",\"yearsOfExperience\": \"string\",\"degreeName\": \"string\",\"certifications\": \"string\",\"company\": {\"description\": \"string\",\"website\": \"string\"}}")))})
  public MappingJacksonValue getPositionProfile(@PathVariable("searchId") String searchId) {
    PositionProfileDTO positionProfile = searchUtil.getPositionProfileDetails(searchId);

    SimpleBeanPropertyFilter companyFilter =
        SimpleBeanPropertyFilter.filterOutAllExcept("description", "website");

    SimpleBeanPropertyFilter positionProfileFilter = SimpleBeanPropertyFilter.filterOutAllExcept(
        "isDegreeMandatory", "isApprovedByPartner", "isYearsOfExperienceMandatory",
        "positionOverview", "productsServicesOverview", "professionalExperience",
        "yearsOfExperience", "degreeName", "certifications", Constant.COMPANY);

    FilterProvider filters =
        new SimpleFilterProvider().addFilter("positionProfileFilter", positionProfileFilter)
            .addFilter(Constant.COMPANY_FILTER, companyFilter);

    MappingJacksonValue mapping = new MappingJacksonValue(positionProfile);
    mapping.setFilters(filters);
    return mapping;
  }

  @Operation(summary = "Get Location Information Details")
  @GetMapping(value = {"/searches/{searchId}/location_info"})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK", content = @Content(
      mediaType = "application/json",
      schema = @Schema(type = "PositionProfileDTO",
          example = "{\"weather\": [{\"title\": \"string\",\"description\": \"string\",\"websiteUrl\": \"string\",\"position\": \"string\"}],\"placeofinterest\": [\r\n"
              + "{\"title\": \"string\",\"description\": \"string\",\"websiteUrl\": \"string\",\"position\": \"string\"}],\"schoolsandcolleges\": [\r\n"
              + "{\"title\": \"string\",\"description\": \"string\",\"websiteUrl\": \"string\",\"position\": \"string\"}],\"restaurantsandshopping\": [\r\n"
              + "{\"title\": \"string\",\"description\": \"string\",\"websiteUrl\": \"string\",\"position\": \"string\"}],\"realestate\": [\r\n"
              + "{\"title\": \"string\",\"description\": \"string\",\"websiteUrl\": \"string\",\"position\": \"string\"}]}")))})
  public MappingJacksonValue getLocationInfo(@PathVariable("searchId") String searchId) {
    PositionProfileDTO positionProfile = searchUtil.getPositionProfileDetails(searchId);

    SimpleBeanPropertyFilter locationFilter = SimpleBeanPropertyFilter.filterOutAllExcept("title",
        "description", "websiteUrl", "position");

    SimpleBeanPropertyFilter positionProfileFilter = SimpleBeanPropertyFilter.filterOutAllExcept(
        "weather", "placeofinterest", "schoolsandcolleges", "restaurantsandshopping", "realestate");

    FilterProvider filters =
        new SimpleFilterProvider().addFilter("positionProfileFilter", positionProfileFilter)
            .addFilter(Constant.LOCATION_FILTER, locationFilter);

    MappingJacksonValue mapping = new MappingJacksonValue(positionProfile);
    mapping.setFilters(filters);
    return mapping;
  }

  @Operation(summary = "Get Search Info Details")
  @GetMapping("/searches/{searchId}")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = "application/json", schema = @Schema(type = "SearchDTO",
          example = "{\"id\": \"string\",\"jobTitle\": \"string\",\"company\": {\"id\": \"string\",\"name\": \"string\"},\"city\": \"string\",\"state\": \"string\"}")))})
  public MappingJacksonValue getCompanyDetails(@PathVariable("searchId") String searchId) {
    SearchDTO searchDTO = searchUtil.getsearchDetails(searchId);

    SimpleBeanPropertyFilter companyFilter =
        SimpleBeanPropertyFilter.filterOutAllExcept(Constant.ID, "name");
    SimpleBeanPropertyFilter searchFilter = SimpleBeanPropertyFilter.filterOutAllExcept(Constant.ID,
        "jobTitle", "company", "city", "state");
    FilterProvider filters = new SimpleFilterProvider().addFilter("searchFilter", searchFilter)
        .addFilter("companyFilter", companyFilter);
    MappingJacksonValue mapping = new MappingJacksonValue(searchDTO);
    mapping.setFilters(filters);

    return mapping;
  }

}
