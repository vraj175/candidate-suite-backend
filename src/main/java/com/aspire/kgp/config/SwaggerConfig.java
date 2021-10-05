package com.aspire.kgp.config;

import java.util.ArrayList;
import java.util.List;

import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.InviteDTO;
import com.aspire.kgp.dto.NotificationSchedulerDTO;
import com.aspire.kgp.dto.ResetPasswordDTO;

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
                "Set Interview notification Schedule", ""))
        .addSecurityItem(
            new SecurityRequirement().addList(Constant.API_KEY).addList(Constant.AUTHORIZATION));
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
                "Language Should be en_US / es_ES / pt_BR"))
        .path(Constant.BASE_API_URL + Constant.PUBLIC_API_URL + "/user/resetPassword",
            getPathItem(getResetPasswordSchema(), "User", "Reset Password for User", ""))
        .addSecurityItem(new SecurityRequirement().addList(Constant.API_KEY));
  }

  /*
   * Generic methods to get PathItem object(For Post API we have to add schemas). For Schema input
   * parameter create schema for particular request and pass it
   */
  private PathItem getPathItem(Schema<?> schema, String tagItem, String summary,
      String description) {
    PathItem pathItem = new PathItem();
    RequestBody requestBody = new RequestBody();

    requestBody.setContent(getRequestContent(schema));

    pathItem.setPost(new Operation().requestBody(requestBody)
        .responses(new ApiResponses().addApiResponse("200",
            new ApiResponse().description("OK").content(getResponseContent())))
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
