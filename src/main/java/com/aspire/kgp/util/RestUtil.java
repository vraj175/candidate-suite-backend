package com.aspire.kgp.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.AWSCognitoIdentityProviderException;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;
import com.amazonaws.services.cognitoidp.model.TooManyRequestsException;
import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.ResponseObject;
import com.aspire.kgp.dto.UserAuthenticationDTO;
import com.aspire.kgp.exception.UnauthorizedAccessException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import net.sf.kdgcommons.lang.StringUtil;
import net.sf.kdgcommons.lang.ThreadUtil;

@Component
public class RestUtil {
  private static final Log log = LogFactory.getLog(RestUtil.class);

  @Value("${galaxy.base.api.url}")
  private String baseApiUrl;
  @Value("${galaxy.api.key}")
  private String apiKey;
  @Value("${galaxy.default.auth}")
  private String defaultAuth;
  @Value("${cognito.client.id}")
  private String clientId;
  @Value("${cognito.user.pool.id}")
  private String userPullId;

  public String newGetMethod(String apiUrl) {
    log.info(baseApiUrl + apiUrl.replaceAll(Constant.SPACE_STRING, "%20"));
    GetMethod get = new GetMethod(baseApiUrl + apiUrl.replaceAll(Constant.SPACE_STRING, "%20"));
    /* set the token in the header */
    get.setRequestHeader(Constant.X_API_KEY, apiKey);
    /* set the token in the header */
    get.setRequestHeader(Constant.AUTHORIZATION,
        validateCognitoWithAuthenticationToken(defaultAuth).getAccessToken());
    String response = "";
    try {
      log.info("Request time: " + new Date());
      new HttpClient().executeMethod(get);
      log.info("Request Code : " + get.getStatusCode());
      response = get.getResponseBodyAsString();
      if (!(apiUrl.contains("/profile-image"))) {
        log.info("Response : " + response);
      }
      log.info("Response time: " + new Date());
    } catch (IOException e) {
      log.error("error while executing query " + e);
    } finally {
      get.releaseConnection();
    }
    return response;
  }

  /*
   * Post Method
   */
  public static String postMethod(String instanceUrl, String serviceUrl, String query) {
    log.debug("In postMethod method");
    /* Configure post method */
    PostMethod postMethod = new PostMethod(instanceUrl + serviceUrl);
    /* Set Data */
    postMethod.setQueryString(query);
    String responseString = null;
    try {
      new HttpClient().executeMethod(postMethod);
      if (postMethod.getStatusCode() == HttpStatus.SC_OK) {
        responseString = postMethod.getResponseBodyAsString();
      }
    } catch (IOException e) {
      log.error("error while getting data ", e);
    }
    log.debug("End of postMethod method");
    return responseString;
  }

  /**
   * Get method used for any REST API
   * 
   * @param url
   * @param accessToken
   * @param paramMap
   * @return
   */
  public void getMethod(String url, OutputStream outputStream) {
    log.debug("In getMethod method");
    log.info("REST URL : " + url);
    /* Configure get method */
    GetMethod get = new GetMethod(baseApiUrl + url.replaceAll(Constant.SPACE_STRING, "%20"));
    /* set the token in the header */
    get.setRequestHeader(Constant.X_API_KEY, apiKey);

    /* Get Data */
    InputStream fileInputStream = null;
    try {
      new HttpClient().executeMethod(get);
      log.info("Status code :: " + get.getStatusCode());
      // Check for unauthorized and if it is 401 it will take new token,
      // save it and call method again.
      if (get.getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
        // executing rest call
        new HttpClient().executeMethod(get);
        log.info("Next Status code :: " + get.getStatusCode());
      }
      if (get.getStatusCode() == HttpStatus.SC_OK) {
        fileInputStream = get.getResponseBodyAsStream();
        readInputStream(fileInputStream, outputStream);
      }
    } catch (IOException e) {
      log.error("error while getting data ", e);
    } finally {
      get.releaseConnection();
    }
    log.debug("End of getMethod method");
  }

  private static void readInputStream(InputStream inputStream, OutputStream outputStream)
      throws IOException {
    byte[] buffer = new byte[1];
    try {
      while (inputStream.read(buffer) > 0) {
        outputStream.write(buffer);
      }
    } catch (Exception e) {
      log.error("Error while getting read input stream", e);
    }
  }

  public byte[] newGetImage(String apiUrl) {
    log.info(baseApiUrl + apiUrl.replaceAll(Constant.SPACE_STRING, "%20"));
    log.debug("API Key : " + apiKey);

    GetMethod get = new GetMethod(baseApiUrl + apiUrl.replaceAll(Constant.SPACE_STRING, "%20"));
    /* set the token in the header */
    get.setRequestHeader(Constant.X_API_KEY, apiKey);
    byte[] response = null;
    try {
      new HttpClient().executeMethod(get);
      log.info("Request Code : " + get.getStatusCode());
      response = get.getResponseBody();
      log.debug("Response : " + response);
    } catch (IOException e) {
      log.error("error while executing query ", e);
      return response;
    } finally {
      get.releaseConnection();
    }
    return response;
  }

  /**
   * Method use for put data
   * 
   * @param url
   * @param sfConfiguration
   * @param paramJSON
   * @return
   * @throws UnsupportedEncodingException
   */
  public String clientLoginDetails(String url, String paramJSON)
      throws UnsupportedEncodingException {
    log.info(baseApiUrl + url);
    log.info("paramJSON:: " + paramJSON);
    HttpClient httpClient = new HttpClient();
    PutMethod httpPut = new PutMethod(baseApiUrl + url);
    httpPut.setRequestHeader(Constant.X_API_KEY, apiKey);
    httpPut.setRequestHeader("Accept", Constant.CONTENT_TYPE_JSON);
    httpPut.setRequestHeader("Content-type", Constant.CONTENT_TYPE_JSON);

    StringRequestEntity body =
        new StringRequestEntity(paramJSON, Constant.CONTENT_TYPE_JSON, "UTF-8");
    httpPut.setRequestEntity(body);

    String responseString = "";
    int statusCode = 0;
    try {
      statusCode = httpClient.executeMethod(httpPut);
    } catch (IOException e1) {
      log.info("IOException : " + e1);
    }

    log.info("-----------Query response time " + new Date());
    try {
      responseString = httpPut.getResponseBodyAsString();
    } catch (IOException e) {
      log.error(e);
    }
    log.info("Status code :: " + statusCode);
    log.info("RESPONSE : " + responseString);
    return responseString;
  }


  /**
   * Get method
   * 
   * @param apiUrl
   * @param accessToken
   * @return
   */
  public String getUserDetails(String accessToken) throws IOException {
    String response = "";
    String apiUrl = baseApiUrl + Constant.GALAXY_API_USER_DETAILS;
    GetMethod get = new GetMethod(apiUrl);
    /* set the token in the header */
    get.setRequestHeader("Authorization", accessToken);
    new HttpClient().executeMethod(get);
    response = get.getResponseBodyAsString();
    return response;
  }

  public static AWSCognitoIdentityProvider getAmazonCognitoIdentityClient() {
    ClasspathPropertiesFileCredentialsProvider propertiesFileCredentialsProvider =
        new ClasspathPropertiesFileCredentialsProvider();

    return AWSCognitoIdentityProviderClientBuilder.standard()
        .withCredentials(propertiesFileCredentialsProvider).withRegion(Regions.US_EAST_2).build();
  }

  public AuthenticationResultType validateCognitoWithAuthenticationToken(String authorization) {
    String userName;
    String password;
    if (authorization != null && authorization.toLowerCase().startsWith("basic")) {
      // Authorization: Basic base64credentials
      String base64Credentials = authorization.substring("Basic".length()).trim();
      byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
      String credentials = new String(credDecoded, StandardCharsets.UTF_8);
      // credentials = username:password
      final String[] values = credentials.split(":", 2);
      userName = values[0];
      password = values[1];
    } else {
      throw new UnauthorizedAccessException(Constant.INVALID_AUTHENTICATION);
    }

    AuthenticationResultType authenticationResult = null;

    final Map<String, String> authParams = new HashMap<>();
    authParams.put("USERNAME", userName);
    authParams.put("PASSWORD", password);

    try {
      AWSCognitoIdentityProvider cognitoClient = getAmazonCognitoIdentityClient();
      final AdminInitiateAuthRequest authRequest = new AdminInitiateAuthRequest();
      authRequest.withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH).withClientId(clientId)
          .withUserPoolId(userPullId).withAuthParameters(authParams);
      log.info("authRequest initialised" + authRequest);

      AdminInitiateAuthResult result = cognitoClient.adminInitiateAuth(authRequest);
      authenticationResult = result.getAuthenticationResult();
      log.info("auth ready");
      cognitoClient.shutdown();
    } catch (Exception e1) {
      log.error(e1.getMessage());
      throw new UnauthorizedAccessException(Constant.INVALID_AUTHENTICATION);
    }
    return authenticationResult;
  }

  public UserAuthenticationDTO validateUserWithCognito(String authorization) {
    log.info("start validateUserWithCognito");
    AuthenticationResultType authenticationResult =
        validateCognitoWithAuthenticationToken(authorization);

    JsonObject userjson;
    String accessToken;
    String refreshToken;
    if (authenticationResult != null) {
      try {
        accessToken = authenticationResult.getAccessToken();
        log.info("AccessToken : " + accessToken);
        refreshToken = authenticationResult.getRefreshToken();

        String authentication = getUserDetails(accessToken);
        userjson = new Gson().fromJson(authentication, JsonObject.class);
        log.info(userjson);
      } catch (Exception e) {
        throw new UnauthorizedAccessException(Constant.INVALID_AUTHENTICATION);
      }
    } else {
      throw new UnauthorizedAccessException(Constant.INVALID_AUTHENTICATION);
    }

    if (userjson.has("id")) {
      return new UserAuthenticationDTO(userjson.get("id").getAsString(),
          userjson.get("name").getAsString(), accessToken, refreshToken);
    } else {
      throw new UnauthorizedAccessException(Constant.INVALID_AUTHENTICATION);
    }

  }

  public ResponseObject attemptRefresh(String refreshToken) {
    String errorMessage = "";
    try {
      Map<String, String> authParams = new HashMap<>();
      authParams.put("REFRESH_TOKEN", refreshToken);

      AdminInitiateAuthRequest refreshRequest =
          new AdminInitiateAuthRequest().withAuthFlow(AuthFlowType.REFRESH_TOKEN)
              .withAuthParameters(authParams).withClientId(clientId).withUserPoolId(userPullId);

      AdminInitiateAuthResult refreshResponse =
          getAmazonCognitoIdentityClient().adminInitiateAuth(refreshRequest);
      if (StringUtil.isBlank(refreshResponse.getChallengeName())) {
        log.info("successfully refreshed token");
        return new ResponseObject(HttpStatus.SC_OK,
            refreshResponse.getAuthenticationResult().getAccessToken(),
            new Timestamp(System.currentTimeMillis()));
      } else {
        log.error(
            "unexpected challenge when refreshing token: {}" + refreshResponse.getChallengeName());
        errorMessage = Constant.REFRESH_TOKEN_EXPIRED;
      }
    } catch (TooManyRequestsException ex) {
      log.info("caught TooManyRequestsException, delaying then retrying");
      ThreadUtil.sleepQuietly(250);
      attemptRefresh(refreshToken);
    } catch (AWSCognitoIdentityProviderException ex) {
      log.info("exception during token refresh: {}" + ex.getMessage());
      errorMessage = Constant.REFRESH_TOKEN_EXPIRED;
    }
    throw new UnauthorizedAccessException(errorMessage);
  }

  public boolean isContactProfileImageExists(String contactId, Map<String, String> imageMap) {
    if (CommonUtil.checkNullString(contactId))
      return false;
    byte[] decodeImg = newGetImage("/clientsuite/contacts/" + contactId + "/profile-image");
    if (decodeImg != null && decodeImg.length > 200) {
      imageMap.put(contactId, "data:image/jpg;base64,"
          + org.apache.commons.codec.binary.Base64.encodeBase64String(decodeImg));
      log.info("image exists");
      return true;
    }
    return false;
  }
}
