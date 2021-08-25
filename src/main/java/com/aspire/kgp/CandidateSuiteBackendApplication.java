package com.aspire.kgp;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMethod;

import com.aspire.kgp.constant.Constant;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import springfox.documentation.RequestHandler;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class CandidateSuiteBackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(CandidateSuiteBackendApplication.class, args);
  }

  /***
   * Use this config for enabling SWAGGER for the application.
   * 
   * @return
   */
  @Bean
  public Docket swaggerConfiguration() {
    return new Docket(DocumentationType.SWAGGER_2).select()
        .paths(Predicates.not(PathSelectors.ant(Constant.USER_AUTHENTICATE_API_URL)))
        .paths(PathSelectors.ant("/api/**"))
        .paths(Predicates
            .not(PathSelectors.ant(Constant.BASE_API_URL + Constant.PUBLIC_API_URL + "/**")))
        .build().securitySchemes(Arrays.asList(accessToken(), apiKey()))
        .securityContexts(Arrays.asList(securityContext()));
  }

  @Bean
  public Docket userAuthentication() {
    return new Docket(DocumentationType.SWAGGER_2).groupName(Constant.USER_AUTHENTICATE_GROUP_NAME)
        .select().apis(httpRequestHandler())
        .paths(PathSelectors.ant(Constant.USER_AUTHENTICATE_API_URL)).build()
        .securitySchemes(Arrays.asList(apiKey()))
        .securityContexts(Arrays.asList(userAuthenticateSecurityContext()));
  }

  @Bean
  public Docket publicAuthentication() {
    return new Docket(DocumentationType.SWAGGER_2).groupName(Constant.PUBLIC_GROUP_NAME).select()
        .paths(PathSelectors.ant(Constant.BASE_API_URL + Constant.PUBLIC_API_URL + "/**")).build()
        .securitySchemes(Arrays.asList(apiKey()))
        .securityContexts(Arrays.asList(publicSecurityContext()));
  }

  private ApiKey apiKey() {
    return new ApiKey(Constant.API_KEY, Constant.API_KEY, Constant.HEADER);
  }

  private ApiKey accessToken() {
    return new ApiKey(Constant.AUTHORIZATION, Constant.AUTHORIZATION, Constant.HEADER);
  }

  private SecurityContext securityContext() {
    return SecurityContext.builder().securityReferences(defaultAuth())
        .forPaths(Predicates
            .not(PathSelectors.ant(Constant.BASE_API_URL + Constant.PUBLIC_API_URL + "/*")))
        .forPaths(PathSelectors.regex(Constant.BASE_API_URL + ".*")).build();
  }

  private SecurityContext userAuthenticateSecurityContext() {
    return SecurityContext.builder().securityReferences(defaultAuth())
        .forPaths(PathSelectors.regex(Constant.USER_AUTHENTICATE_API_URL)).build();
  }

  private SecurityContext publicSecurityContext() {
    return SecurityContext.builder().securityReferences(defaultAuth())
        .forPaths(PathSelectors.regex(Constant.BASE_API_URL + ".*")).build();
  }

  private List<SecurityReference> defaultAuth() {
    AuthorizationScope authorizationScope = new AuthorizationScope(
        Constant.GLOBAL_AUTHORIZATION_SCOPE, Constant.ACCESS_EVERYTHING_AUTHORZATION_SCOPE);
    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
    authorizationScopes[0] = authorizationScope;
    return Arrays.asList(new SecurityReference(Constant.AUTHORIZATION, authorizationScopes),
        new SecurityReference(Constant.API_KEY, authorizationScopes),
        new SecurityReference(Constant.GRANT_TYPE, authorizationScopes));
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
