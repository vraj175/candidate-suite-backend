package com.aspire.kgp;

import java.util.ArrayList;
import java.util.List;

import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.aspire.kgp.constant.Constant;

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
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class CandidateSuiteBackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(CandidateSuiteBackendApplication.class, args);
  }

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI().components(new Components())
        .info(new Info().title("Candidatesuite Application API").description(
            "This is a sample Spring Boot RESTful service using springdoc-openapi and OpenAPI 3.")
            .version("1.0"));
  }

  /***
   * Use this config for enabling SWAGGER for the application.
   * 
   * @return
   */
  @Bean
  public GroupedOpenApi swaggerConfiguration() {
    return GroupedOpenApi.builder().group("default")
        .pathsToExclude(Constant.BASE_API_URL + Constant.PUBLIC_API_URL + "/**",
            Constant.USER_AUTHENTICATE_API_URL)
        .pathsToMatch("/api/v1.0/candidates/**", "/api/v1.0/searches/**", "/api/v1.0/companies/**",
            "/api/v1.0/company/**", "/api/v1.0/companyInfo/**", "/api/v1.0/languages/**",
            "/api/v1.0/roles/**", "/api/v1.0/profile/**", "/api/v1.0/user/**", "/api/v1.0/video/**")
        .addOpenApiCustomiser(defaultAPIConfig()).build();
  }

  @Bean
  public GroupedOpenApi publicAuthentication() {
    return GroupedOpenApi.builder().group(Constant.PUBLIC_GROUP_NAME)
        .pathsToMatch(Constant.BASE_API_URL + Constant.PUBLIC_API_URL + "/**")
        .addOpenApiCustomiser(publicAPIConfig()).build();
  }

  @Bean
  public GroupedOpenApi userAuthentication() {
    return GroupedOpenApi.builder().group(Constant.USER_AUTHENTICATE_GROUP_NAME)
        .pathsToMatch(Constant.USER_AUTHENTICATE_API_URL)
        .addOpenApiCustomiser(userAuthenticationAPIConfig()).build();
  }

  private OpenApiCustomiser defaultAPIConfig() {
    return openApi -> openApi
        .components(new Components()
            .addSecuritySchemes(Constant.API_KEY,
                new SecurityScheme().type(SecurityScheme.Type.APIKEY).in(SecurityScheme.In.HEADER)
                    .name(Constant.API_KEY))
            .addSecuritySchemes(Constant.AUTHORIZATION,
                new SecurityScheme().type(SecurityScheme.Type.APIKEY).in(SecurityScheme.In.HEADER)
                    .name(Constant.AUTHORIZATION)))
        .addSecurityItem(
            new SecurityRequirement().addList(Constant.API_KEY).addList(Constant.AUTHORIZATION));
  }

  private OpenApiCustomiser publicAPIConfig() {
    return openApi -> openApi
        .components(new Components().addSecuritySchemes(Constant.API_KEY,
            new SecurityScheme().type(SecurityScheme.Type.APIKEY).in(SecurityScheme.In.HEADER)
                .name(Constant.API_KEY)))
        .addSecurityItem(new SecurityRequirement().addList(Constant.API_KEY));
  }

  private OpenApiCustomiser userAuthenticationAPIConfig() {
    PathItem pathItem = new PathItem();
    RequestBody requestBody = new RequestBody();

    Schema<?> propSchema = new Schema<>();
    Schema<?> requestSchema = new Schema<>();
    Schema<String> grantTypeSchema = new Schema<>();
    List<String> grantTypeList = new ArrayList<String>();
    grantTypeList.add(Constant.PASSWORD);
    grantTypeList.add(Constant.REFRESH_TOKEN);
    grantTypeSchema.setEnum(grantTypeList);

    requestSchema.setType("object");
    requestSchema.addProperties(Constant.GRANT_TYPE.toLowerCase(), grantTypeSchema);
    requestSchema.addProperties(Constant.USER_NAME, propSchema);
    requestSchema.addProperties(Constant.PASSWORD, propSchema);
    requestSchema.addProperties(Constant.REFRESH_TOKEN, propSchema);
    List<String> requireParameterList = new ArrayList<String>();
    requireParameterList.add("grant_type");
    requestSchema.required(requireParameterList);

    Content requestContent = new Content();
    requestContent.addMediaType("multipart/form-data", new MediaType().schema(requestSchema));
    requestBody.setContent(requestContent);

    Content responseContent = new Content();
    responseContent.addMediaType("application/json", new MediaType().schema(new Schema<>().example(
        "{\"access_token\": \"string\",\"token_type\": \"string\",\"refresh_token\": \"string\",\"expires_in\": 0,\"scope\": \"string\",\"jti\": \"string\"}")));
    pathItem.setPost(new Operation().requestBody(requestBody)
        .responses(new ApiResponses().addApiResponse("200",
            new ApiResponse().description("OK").content(responseContent)))
        .addTagsItem("User Authentication").description(Constant.USER_AUTHENTICATION_DESC)
        .summary("Get Refresh Token"));

    return openApi -> openApi
        .components(new Components().addSecuritySchemes(Constant.API_KEY,
            new SecurityScheme().type(SecurityScheme.Type.APIKEY).in(SecurityScheme.In.HEADER)
                .name(Constant.API_KEY)))
        .path(Constant.USER_AUTHENTICATE_API_URL, pathItem)
        .addSecurityItem(new SecurityRequirement().addList(Constant.API_KEY));
  }

  /***
   * To remove Models section from Swagger UI available on http://localhost:8080/swagger-ui.html
   * 
   * @return
   */
  @Bean
  UiConfiguration uiConfig() {
    return UiConfigurationBuilder.builder().defaultModelsExpandDepth(-1).build();
  }
}
