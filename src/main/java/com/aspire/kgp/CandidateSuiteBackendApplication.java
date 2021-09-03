package com.aspire.kgp;

import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMethod;

import com.aspire.kgp.constant.Constant;
import com.google.common.base.Predicate;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import springfox.documentation.RequestHandler;
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
            "/api/v1.0/roles/**", "/api/v1.0/profile/**", "/api/v1.0/user/**")
        .addOpenApiCustomiser(defaultAPIConfig()).build();
  }

  @Bean
  public GroupedOpenApi publicAuthentication() {
    return GroupedOpenApi.builder().group(Constant.PUBLIC_GROUP_NAME)
        .pathsToMatch(Constant.BASE_API_URL + Constant.PUBLIC_API_URL + "/**")
        .addOpenApiCustomiser(apiConfig()).build();
  }

  @Bean
  public GroupedOpenApi userAuthentication() {
    return GroupedOpenApi.builder().group(Constant.USER_AUTHENTICATE_GROUP_NAME)
        .pathsToMatch(Constant.USER_AUTHENTICATE_API_URL).addOpenApiCustomiser(apiConfig()).build();
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

  private OpenApiCustomiser apiConfig() {
    return openApi -> openApi
        .components(new Components().addSecuritySchemes(Constant.API_KEY,
            new SecurityScheme().type(SecurityScheme.Type.APIKEY).in(SecurityScheme.In.HEADER)
                .name(Constant.API_KEY)))
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

  private Predicate<RequestHandler> httpRequestHandler() {
    return p -> p.supportedMethods().contains(RequestMethod.POST);
  }
}
