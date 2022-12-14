package com.aspire.kgp.config;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.CandidateFeedbackRequestDTO;
import com.aspire.kgp.dto.EducationDTO;
import com.aspire.kgp.dto.InviteDTO;
import com.aspire.kgp.dto.NotificationSchedulerDTO;
import com.aspire.kgp.dto.NotificationsDTO;
import com.aspire.kgp.dto.ResetPasswordDTO;
import com.aspire.kgp.model.BoardHistory;
import com.aspire.kgp.model.Contact;
import com.aspire.kgp.model.JobHistory;
import com.aspire.kgp.model.Reference;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {


  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI().components(new Components())
        .info(new Info().title("Candidatesuite Application API").description(
            "This is a sample Spring Boot RESTful service using springdoc-openapi and OpenAPI 3.")
            .version("1.0"))
        .addServersItem(new Server().url("/").description("Default URL"));
  }

  /*
   * Default Group configuration
   */
  @Bean
  public GroupedOpenApi swaggerConfiguration() {
    return GroupedOpenApi.builder().group("default")
        .pathsToExclude(Constant.BASE_API_URL + Constant.PUBLIC_API_URL + "/**",
            Constant.USER_AUTHENTICATE_API_URL)
        .pathsToMatch("/api/v1.0/candidates/**", "/api/v1.0/contact/**", "/api/v1.0/searches/**",
            "/api/v1.0/companies/**", "/api/v1.0/company/**", "/api/v1.0/companyInfo/**",
            "/api/v1.0/languages/**", "/api/v1.0/roles/**", "/api/v1.0/profile/**",
            "/api/v1.0/user/**", "/api/v1.0/video/**", "/api/v1.0/picklists/**",
            "/api/v1.0/companyName/**", "/api/v1.0/contactName/**", "/api/v1.0/notification/**")
        .addOpenApiCustomiser(defaultAPIConfig()).build();
  }

  /*
   * Public group Configuration
   */
  @Bean
  public GroupedOpenApi publicAuthentication() {
    return GroupedOpenApi.builder().group(Constant.PUBLIC_GROUP_NAME)
        .pathsToMatch(Constant.BASE_API_URL + Constant.PUBLIC_API_URL + "/**")
        .addOpenApiCustomiser(publicAPIConfig()).build();
  }

  /*
   * User Group Configuration
   */
  @Bean
  public GroupedOpenApi userAuthentication() {
    return GroupedOpenApi.builder().group(Constant.USER_AUTHENTICATE_GROUP_NAME)
        .pathsToMatch(Constant.USER_AUTHENTICATE_API_URL)
        .addOpenApiCustomiser(userAuthenticationAPIConfig()).build();
  }

  /*
   * Default Group API's header parameters configuration
   */
  private OpenApiCustomiser defaultAPIConfig() {
    return openApi -> openApi
        .components(new Components()
            .addSecuritySchemes(Constant.API_KEY,
                new SecurityScheme().type(SecurityScheme.Type.APIKEY).in(SecurityScheme.In.HEADER)
                    .name(Constant.API_KEY))
            .addSecuritySchemes(Constant.AUTHORIZATION,
                new SecurityScheme().type(SecurityScheme.Type.APIKEY).in(SecurityScheme.In.HEADER)
                    .name(Constant.AUTHORIZATION)))
        .path(Constant.BASE_API_URL + "/notification",
            getPathItem(getNotificationSchedulerSchema(), "Notification",
                "Set Interview notification Schedule", "", getResponseContent()))
        .path(Constant.BASE_API_URL + "/contact/reference/{referenceId}",
            getPutRequestPathItem(getContactReferenceResponseContent(), "Contact",
                "Update Contact Reference", "", getContactResponseContent()))
        .path(Constant.BASE_API_URL + "/contact/{contactId}/references",
            getPathItem(getContactReferenceResponseContent(), "Contact", "Add Contact Reference",
                "", getContactResponseContent()))
        .path(Constant.BASE_API_URL + "/notification/add",
            getPathItem(getNotificationDTOSchema(), "Notification", "Add New Notification", "",
                getResponseContent()))
        .path(Constant.BASE_API_URL + "/candidates/candidate-feedback",
            getPathItem(getCandidateFeedbackDTOSchema(), "Candidate", "Add Candidate Feedback", "",
                getCandidateFeedbackResponseContent()))
        .path(Constant.BASE_API_URL + "/candidates/candidate-feedback/reply",
            getPathItem(getCandidateFeedbackReplyDTOSchema(), "Candidate",
                "Add Candidate Feedback Reply", "", getCandidateFeedbackReplyResponseContent()))
        .path(Constant.BASE_API_URL + "/candidates/candidate-feedback/status-update",
            getPutRequestPathItem(getCandidateFeedbackUpdateDTOSchema(), "Candidate",
                "Update Candidate Feedback Status", "", getCandidateFeedbackResponseContent()))
        .path(Constant.BASE_API_URL + "/contact/update/{contactId}",
            getPutRequestPathItem(getContactUpdateDTOSchema(), "Contact", "Update Contact Details",
                "", getContactResponseContent()))
        .path(Constant.BASE_API_URL + "/contact/education/{contactId}",
            getPutRequestPathItem(getContactEducationDTOSchema(), "Contact",
                "Update Contact Education Details", "", getContactResponseContent()))
        .addSecurityItem(
            new SecurityRequirement().addList(Constant.API_KEY).addList(Constant.AUTHORIZATION));
  }

  /* Schema For update refernce Contact Update */
  private Schema<Reference> getContactReferenceResponseContent() {
    Schema<Reference> contactUpdateSchema = new Schema<>();
    Reference reference = new Reference();
    reference.setId(0);
    reference.setContactId(Constant.STRING);
    reference.setRefContactName(Constant.STRING);
    reference.setSearchName(Constant.STRING);
    reference.setSearchId(Constant.STRING);
    reference.setPhone(Constant.STRING);
    reference.setEmail(Constant.STRING);
    reference.setWorkEmail(Constant.STRING);
    reference.setRelationship(Constant.STRING);
    reference.setRefType(Constant.STRING);
    reference.setCompanyName(Constant.STRING);
    reference.setTitle(Constant.STRING);
    reference.setCreatedDate(new Timestamp(System.currentTimeMillis()));
    reference.setModifyDate(new Timestamp(System.currentTimeMillis()));
    contactUpdateSchema.addEnumItemObject(reference);
    return contactUpdateSchema;
  }

  /*
   * Public Group API's header parameters configuration
   */
  private OpenApiCustomiser publicAPIConfig() {

    return openApi -> openApi
        .components(new Components().addSecuritySchemes(Constant.API_KEY,
            new SecurityScheme().type(SecurityScheme.Type.APIKEY).in(SecurityScheme.In.HEADER)
                .name(Constant.API_KEY)))
        .path(Constant.BASE_API_URL + Constant.PUBLIC_API_URL + "/user/invite",
            getPathItem(getInviteSchema(), "User", "Invite User as Candidates",
                "Language Should be en_US / es_ES / pt_BR", getResponseContent()))
        .path(Constant.BASE_API_URL + Constant.PUBLIC_API_URL + "/user/resetPassword",
            getPathItem(getResetPasswordSchema(), "User", "Reset Password for User", "",
                getResponseContent()))
        .addSecurityItem(new SecurityRequirement().addList(Constant.API_KEY));
  }

  /*
   * Generic methods to get PathItem object(For Post API we have to add schemas). For Schema input
   * parameter create schema for particular request and pass it
   */
  private PathItem getPathItem(Schema<?> schema, String tagItem, String summary, String description,
      Content responseContent) {
    PathItem pathItem = new PathItem();
    RequestBody requestBody = new RequestBody();

    requestBody.setContent(getRequestContent(schema));

    pathItem.setPost(new Operation().requestBody(requestBody)
        .responses(new ApiResponses().addApiResponse("200",
            new ApiResponse().description("OK").content(responseContent)))
        .addTagsItem(tagItem).summary(summary).description(description));
    return pathItem;
  }

  /*
   * Generic methods to get PathItem object(For PUT API we have to add schemas). For Schema input
   * parameter create schema for particular request and pass it
   */
  private PathItem getPutRequestPathItem(Schema<?> schema, String tagItem, String summary,
      String description, Content responseContent) {
    PathItem pathItem = new PathItem();
    RequestBody requestBody = new RequestBody();

    requestBody.setContent(getRequestContent(schema));

    pathItem.setPut(new Operation().requestBody(requestBody)
        .responses(new ApiResponses().addApiResponse("200",
            new ApiResponse().description("OK").content(responseContent)))
        .addTagsItem(tagItem).summary(summary).description(description));
    return pathItem;
  }

  /*
   * Schema For invite User API
   */
  private Schema<InviteDTO> getInviteSchema() {
    Schema<InviteDTO> inviteSchema = new Schema<>();
    InviteDTO invaite = new InviteDTO();
    invaite.setCandidateId(Constant.STRING);
    invaite.setLanguage(Constant.STRING);
    invaite.setEmail(Constant.STRING);
    invaite.setRemoveDuplicate(false);
    invaite.setPartnerId(Constant.STRING);
    String[] ddd = new String[1];
    ddd[0] = Constant.STRING;
    invaite.setBcc(ddd);
    inviteSchema.addEnumItemObject(invaite);
    return inviteSchema;
  }

  /*
   * Schema For Reset Password API
   */
  private Schema<ResetPasswordDTO> getResetPasswordSchema() {
    Schema<ResetPasswordDTO> resetPasswordSchema = new Schema<>();
    ResetPasswordDTO resetPassword = new ResetPasswordDTO();
    resetPassword.setEmail(Constant.STRING);
    resetPassword.setOldPassword(Constant.STRING);
    resetPassword.setNewPassword(Constant.STRING);
    resetPasswordSchema.addEnumItemObject(resetPassword);
    return resetPasswordSchema;
  }

  /*
   * Schema For Send Interview Notification Scheduler API
   */
  private Schema<NotificationSchedulerDTO> getNotificationSchedulerSchema() {
    Schema<NotificationSchedulerDTO> notificationSchedulerSchema = new Schema<>();
    NotificationSchedulerDTO notificationScheduler = new NotificationSchedulerDTO();
    notificationScheduler.setCandidateId(Constant.STRING);
    notificationScheduler.setScheduleId(Constant.STRING);
    notificationScheduler.setDate(Constant.STRING);
    notificationScheduler.setMessage(Constant.STRING);
    notificationSchedulerSchema.addEnumItemObject(notificationScheduler);
    return notificationSchedulerSchema;
  }

  /* Schema For add new notification */

  private Schema<NotificationsDTO> getNotificationDTOSchema() {
    Schema<NotificationsDTO> notificationDTOSchema = new Schema<>();
    NotificationsDTO notificationsDTO = new NotificationsDTO();
    notificationsDTO.setDescription(Constant.STRING);
    notificationDTOSchema.addEnumItemObject(notificationsDTO);
    return notificationDTOSchema;
  }

  /* Schema For add candidate Feedback */
  private Schema<?> getCandidateFeedbackDTOSchema() {
    Schema<CandidateFeedbackRequestDTO> candidateFeedbackRequestSchema = new Schema<>();
    CandidateFeedbackRequestDTO feedback = new CandidateFeedbackRequestDTO();
    feedback.setComments(Constant.STRING);
    feedback.setCandidateId(Constant.STRING);
    candidateFeedbackRequestSchema.addEnumItemObject(feedback);
    return candidateFeedbackRequestSchema;
  }

  /* Schema For add candidate Feedback */
  private Schema<?> getCandidateFeedbackUpdateDTOSchema() {
    Schema<CandidateFeedbackRequestDTO> candidateFeedbackRequestSchema = new Schema<>();
    CandidateFeedbackRequestDTO feedback = new CandidateFeedbackRequestDTO();
    feedback.setCommentId(Constant.STRING);
    feedback.setStatus(false);
    candidateFeedbackRequestSchema.addEnumItemObject(feedback);
    return candidateFeedbackRequestSchema;
  }

  /* Schema For add Contact Update */
  private Schema<?> getContactUpdateDTOSchema() {
    Schema<Contact> contactUpdateSchema = new Schema<>();
    Contact contact = new Contact();
    JobHistory jobHistory = new JobHistory();
    BoardHistory boardHistory = new BoardHistory();
    EducationDTO educationDTO = new EducationDTO();
    List<JobHistory> jobHistories = new ArrayList<>();
    List<BoardHistory> boardHistories = new ArrayList<>();
    List<EducationDTO> educationList = new ArrayList<>();
    contact.setId(0);
    contact.setGalaxyId(Constant.STRING);
    contact.setCompany(Constant.STRING);
    contact.setFirstName(Constant.STRING);
    contact.setLastName(Constant.STRING);
    contact.setCity(Constant.STRING);
    contact.setState(Constant.STRING);
    contact.setWorkEmail(Constant.STRING);
    contact.setEmail(Constant.STRING);
    contact.setLinkedInUrl(Constant.STRING);
    contact.setMobilePhone(Constant.STRING);
    contact.setCurrentJobTitle(Constant.STRING);
    contact.setHomePhone(Constant.STRING);
    jobHistory.setId(0);
    jobHistory.setTitle(Constant.STRING);
    jobHistory.setStartYear(Constant.STRING);
    jobHistory.setEndYear(Constant.STRING);
    jobHistory.setCompany(Constant.STRING);
    jobHistories.add(jobHistory);
    boardHistory.setId(0);
    boardHistory.setTitle(Constant.STRING);
    boardHistory.setCompany(Constant.STRING);
    boardHistory.setStartYear(Constant.STRING);
    boardHistory.setEndYear(Constant.STRING);
    boardHistory.setCommitee(Constant.STRING);
    boardHistories.add(boardHistory);
    educationDTO.setId(Constant.STRING);
    educationDTO.setMajor(Constant.STRING);
    educationDTO.setSchoolName(Constant.STRING);
    educationDTO.setDegreeName(Constant.STRING);
    educationDTO.setDegreeYear(Constant.STRING);
    educationList.add(educationDTO);
    contact.setJobHistory(jobHistories);
    contact.setBoardHistory(boardHistories);
    contact.setEducationDetails(educationList);
    contactUpdateSchema.addEnumItemObject(contact);
    return contactUpdateSchema;
  }

  /* Schema For add Contact Education Update */
  private Schema<?> getContactEducationDTOSchema() {
    Schema<List<EducationDTO>> contactUpdateEducationSchema = new Schema<>();
    List<EducationDTO> educationList = new ArrayList<>();
    EducationDTO educationDTO = new EducationDTO();
    educationDTO.setId(Constant.STRING);
    educationDTO.setMajor(Constant.STRING);
    educationDTO.setSchoolName(Constant.STRING);
    educationDTO.setDegreeName(Constant.STRING);
    educationDTO.setDegreeYear(Constant.STRING);
    educationList.add(educationDTO);
    contactUpdateEducationSchema.addEnumItemObject(educationList);
    return contactUpdateEducationSchema;
  }

  /* Schema For add candidate Feedback Reply */
  private Schema<?> getCandidateFeedbackReplyDTOSchema() {
    Schema<CandidateFeedbackRequestDTO> candidateFeedbackRequestSchema = new Schema<>();
    CandidateFeedbackRequestDTO feedbackReply = new CandidateFeedbackRequestDTO();
    feedbackReply.setCandidateId(Constant.STRING);
    feedbackReply.setCommentId(Constant.STRING);
    feedbackReply.setReply(Constant.STRING);
    candidateFeedbackRequestSchema.addEnumItemObject(feedbackReply);
    return candidateFeedbackRequestSchema;
  }

  /*
   * Used for request content show on UI
   */
  private Content getRequestContent(Schema<?> schema) {
    Content requestContent = new Content();
    return requestContent.addMediaType(Constant.CONTENT_TYPE_JSON, new MediaType().schema(schema));

  }

  /*
   * Used For response content show on UI
   */
  private Content getResponseContent() {
    Content responseContent = new Content();
    return responseContent.addMediaType(Constant.CONTENT_TYPE_JSON,
        new MediaType().schema(new Schema<>().example(
            "{\"timestamp\": \"string\",\"status\": \"string\",\"message\": \"string\"}")));
  }

  /*
   * Used For candidate Feedback response content show on UI
   */
  private Content getCandidateFeedbackResponseContent() {
    Content responseContent = new Content();
    return responseContent.addMediaType(Constant.CONTENT_TYPE_JSON,
        new MediaType().schema(new Schema<>().example("{\"id\": \"string\"}")));
  }

  /*
   * Used For contact Update response content show on UI
   */
  private Content getContactResponseContent() {
    Content responseContent = new Content();
    return responseContent.addMediaType(Constant.CONTENT_TYPE_JSON,
        new MediaType().schema(new Schema<>().example("{\"message\": \"string\"}")));
  }

  /*
   * 
   * /* Used For candidate Feedback response content show on UI
   */
  private Content getCandidateFeedbackReplyResponseContent() {
    Content responseContent = new Content();
    return responseContent.addMediaType(Constant.CONTENT_TYPE_JSON,
        new MediaType().schema(new Schema<>().example(
            "{\"id\": \"string\",\"candidateId\": \"string\",\"createdBy\": \"string\",\"createdAt\": \"string\",\"updatedAt\": \"string\",\"type\": \"string\",\"createdName\": \"string\",\"comments\": \"string\",\"status\": false,\"replies\": [{\"id\": \"string\",\"candidateId\": \"string\",\"createdBy\": \"string\",\"createdAt\": \"string\",\"updatedAt\": \"string\",\"commentId\": \"string\",\"reply\": \"string\",\"type\": \"string\",\"createdName\": \"string\"}]}")));
  }

  /*
   * User Group Authentication API Configuration
   */
  private OpenApiCustomiser userAuthenticationAPIConfig() {
    PathItem pathItem = new PathItem();
    RequestBody requestBody = new RequestBody();

    Schema<?> propSchema = new Schema<>();
    Schema<?> requestSchema = new Schema<>();
    Schema<String> grantTypeSchema = new Schema<>();
    List<String> grantTypeList = new ArrayList<>();
    grantTypeList.add(Constant.PASSWORD);
    grantTypeList.add(Constant.REFRESH_TOKEN);
    grantTypeSchema.setEnum(grantTypeList);

    requestSchema.setType("object");
    requestSchema.addProperties(Constant.GRANT_TYPE.toLowerCase(), grantTypeSchema);
    requestSchema.addProperties(Constant.USER_NAME, propSchema);
    requestSchema.addProperties(Constant.PASSWORD, propSchema);
    requestSchema.addProperties(Constant.REFRESH_TOKEN, propSchema);
    List<String> requireParameterList = new ArrayList<>();
    requireParameterList.add("grant_type");
    requestSchema.required(requireParameterList);

    Content requestContent = new Content();
    requestContent.addMediaType("multipart/form-data", new MediaType().schema(requestSchema));
    requestBody.setContent(requestContent);

    Content responseContent = new Content();
    responseContent.addMediaType(Constant.CONTENT_TYPE_JSON,
        new MediaType().schema(new Schema<>().example(
            "{\"access_token\": \"string\",\"token_type\": \"string\",\"refresh_token\": \"string\",\"expires_in\": 0,\"scope\": \"string\",\"jti\": \"string\"}")));
    pathItem.setPost(new Operation().requestBody(requestBody)
        .responses(new ApiResponses().addApiResponse("200",
            new ApiResponse().description("OK").content(responseContent)))
        .addTagsItem("User Authentication").description(Constant.USER_AUTHENTICATION_DESC)
        .summary("Get Authentication Token"));

    return openApi -> openApi
        .components(new Components().addSecuritySchemes(Constant.API_KEY,
            new SecurityScheme().type(SecurityScheme.Type.APIKEY).in(SecurityScheme.In.HEADER)
                .name(Constant.API_KEY)))
        .path(Constant.USER_AUTHENTICATE_API_URL, pathItem)
        .addSecurityItem(new SecurityRequirement().addList(Constant.API_KEY));
  }
}
