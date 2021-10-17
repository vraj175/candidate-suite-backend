package com.aspire.kgp.constant;

public class Constant {

  private Constant() {}

  public static final String UTF_8 = "UTF-8";
  public static final String ACCESS_TOKEN_X_TOKEN = "X-Token";
  public static final String X_API_KEY = "X-API-Key";
  public static final String TIMESTAMP = "timestamp";
  public static final String STATUS = "status";
  public static final String ERROR = "error";
  public static final String COMPLETED = "Completed";
  public static final String MESSAGE = "message";
  public static final String SQL_EXCEPTION = "SQL Exception :";
  public static final String MISSING_API_KEY = "Missing API key.";
  public static final String MISSING_REQUEST_HEADER = "Missing request header.";
  public static final String PATH = "path";
  public static final String DEFAULT = "Default";
  public static final String AUTHORIZATION = "Authorization";
  public static final String API_KEY = "Api_key";
  public static final String API_SECRET_KEY = "apiSecretKey";
  public static final String API_EXCEPTION = "An exception was thrown!";
  public static final String GRANT_TYPE = "Grant_type";
  public static final String INVALID_GRANT_TYPE = "Invalid Grant_type";
  public static final String PASSWORD = "password";
  public static final String USER_NAME = "username";
  public static final String TOKEN = "token";
  public static final String REFRESH_TOKEN = "refresh_token";
  public static final String SUCCESS = "success";
  public static final String INVALID_AUTHENTICATION = "Invalid Authentication";
  public static final String INVALID_REPORT_ID = "Invalid Report Id";
  public static final String INVALID_API_KEY = "Invalid API Key";
  public static final String REFRESH_TOKEN_EXPIRED = "Refresh Token Expired";
  public static final String BAD_REQUEST = "Bad Request";
  public static final String CONTENT_TYPE_JSON = "application/json";

  /* Languages */
  public static final String ENGLISH = "English";
  public static final String SPANISH = "Español";
  public static final String PORTUGUESE = "Português";

  public static final String ENGLISH_CODE = "en_US";
  public static final String SPANISH_CODE = "es_ES";
  public static final String PORTUGUESE_CODE = "pt_BR";

  /* Roles */
  public static final String PARTNER = "partner";
  public static final String CANDIDATE = "candidate";

  /* Emails */
  public static final String FROM_MAIL = "candidatesuite@kingsleygate.com";
  public static final String SENDER_NAME = "Candidate Suite";
  public static final String INVITE_SUBJECT = "Welcome to Kingsley Gate Partners Portal!";
  public static final String FORGOT_PASSWORD_SUBJECT = "Forgot Password";
  public static final String FILE_UPLOADED_SUCCESSFULLY = "File uploaded successfully";
  public static final String IMAGE_UPLOADED_SUCCESSFULLY = "Profile image uploaded successfully";

  public static final String CLIENT_SUITE_PROD_SERVER_URL = "https://clients.kingsleygate.com";
  public static final String PARSING_ERROR = "Exception while parsing";
  public static final String ILLEGAL_ARGUMENT_EXCEPTION = "Illegal Argument Exception";
  public static final String JSON_PROCESSING_EXCEPTION = "JsonProcessingException : ";
  public static final String INVALID_CANDIDATE_ID = "Invalid Candidate Id";
  public static final String FILE_UPLOAD_ERROR = "Error in upload file";
  public static final String IMAGE_UPLOAD_ERROR = "Error in upload profile image";
  public static final String CONVERT_JSON_ERROR = "Error in coverting json to object";

  public static final String EMPTY_STRING = "";
  public static final String ASC = "ASC";
  public static final String DESC = "DESC";
  // Api url
  public static final String USER_PROFILE_URL = "/users/profile";
  public static final String CONDIDATE_URL = "/candidates/{candidateId}";
  public static final String CONTACT_URL = "/contacts/{contactId}";
  public static final String USER_URL = "/users/{userId}";
  public static final String COMPANY_LIST = "/clientsuite/partner/companies?stage={STAGE}";
  public static final String SEARCHES_LIST_BY_COMAPNY =
      "/clientsuite/partner/companies/{companyId}/searches";
  public static final String SEARCHES_LIST_BY_IDS = "/searches/search-details-by-ids";
  public static final String CANDIDATE_LIST_URL = "/searches/{searchId}/candidates";
  public static final String POSITION_PROFILE_URL = "/searches/{searchId}/sfpa";
  public static final String CANDIDATE_URL = "/candidates/{candidateId}/activity";
  public static final String CANDIDATE_FEEDBACK_URL =
      "/candidate-suite/candidates/{candidateId}/candidate-feedback";
  public static final String CANDIDATE_FEEDBACK_REPLY_URL =
      "/candidate-suite/candidate-feedback/{commentId}/{candidateId}/reply";

  public static final String CONTACT_PROFILE_IMAGE_URL =
      "/clientsuite/contacts/{contactId}/profile-image";
  public static final String GET_COMPANY_LIST_URL = "/synclink/companies?name={COMPANYNAME}";
  public static final String DOWNLOAD_ATTACHMENT =
      "/clientsuite/candidate/resumes/{attachmentId}/download";
  public static final String SEARCH_INFO_URL = "/searches/{SEARCHID}";
  public static final String EDUCATION_DEGREE_PICKLIST_URL = "/picklists/educations/degrees";
  public static final String CONTACT_REFERENCE_URL = "/contact/{contactId}/references";
  public static final String CONTACT_SEARCHES_URL =
      "/picklists/searches?searchValue=&contactId={contactId}&stage=Open";
  public static final String GET_CONTACT_LIST_BY_NAME_URL =
      "/picklists/contacts?name={CONTACTNAME}";
  public static final String REFERENCE_TYPE_PICKLIST_URL = "/picklists/reference-types";
  public static final String COUNTRIES_PICKLIST_URL = "/picklists/countries";
  public static final String INDUSTRES_PICKLIST_URL = "/synclink/picklists/industries";
  public static final String CONTACT_SAVE_URL = "/contacts";
  public static final String COMPANY_SAVE_URL = "/companies";

  public static final String RESUME_URL = "/contact/{contactId}/resumes";
  public static final String ATTECHMENT_URL = "/contact/{contactId}/attachments";
  public static final String IMAGE_UPLOAD_URL = "/contacts/{contactId}/image";
  public static final String UPDATE_CONTACT_REFERENCE_URL = "/contact/references/{referenceId}";
  public static final String CONTACT_ID = "{contactId}";
  public static final String CANDIDATE_ID_BRACES = "{candidateId}";

  // Queries parameter
  public static final String INVALID_JSON_ERROR = "oops ! invalid json";
  public static final String SPACE_STRING = "[\\s]";

  public static final String STRING_LOCALHOST = "localhost";
  public static final String STRING_HTTPS = "https";
  public static final String STRING_HTTP = "http";

  public static final String GALAXY_API_USER_DETAILS = "/users/profile";
  public static final String USER_AUTHENTICATE_API_URL = "/api/v1.0/oauth/token";
  public static final String USER_AUTHENTICATE_GROUP_NAME = "user-authenticate";
  public static final String PUBLIC_GROUP_NAME = "public";
  public static final String PUBLIC_API_URL = "/open";
  public static final String BASE_API_URL = "/api/v1.0";
  public static final String HEADER = "header";
  public static final String GLOBAL_AUTHORIZATION_SCOPE = "global";
  public static final String ACCESS_EVERYTHING_AUTHORZATION_SCOPE = "accessEverything";

  public static final String INFORMATION_FORTHCOMING = "Information forthcoming";

  public static final String STRING_DATA = "data";
  public static final String STRING_PAGING = "paging";

  public static final String NUMBER_PATTERN = "\\d+(\\.\\d+)?";
  public static final String NOT_NUMBER_PATTERN = "[^(" + NUMBER_PATTERN + ")]";
  public static final String UNIT_PATTERN = "(" + NUMBER_PATTERN + ")[kKmMbB]";
  public static final String TEST_HTML_DATA = "<!DOCTYPE html><html></html>";

  public static final String EMAILS_CONTENT_MAP = "emailsStaticContents";
  public static final String CANDIDATE_INVITE_EMAIL_TEMPLATE = "candidate-invitation.ftl";
  public static final String FORGOT_EMAIL_TEMPLATE = "forgot-password.ftl";
  public static final String CANDIDATE_FEEDBACK_EMAIL_TEMPLATE = "candidate-feedback.ftl";

  // Json Fields
  public static final String FIRST_NAME = "firstName";
  public static final String LAST_NAME = "lastName";
  public static final String CITY = "city";
  public static final String STATE = "state";
  public static final String ID = "id";
  public static final String CONTACT = "contact";
  public static final String SEARCH = "search";
  public static final String JOB_TITLE = "jobTitle";
  public static final String JOB_NUMBER = "jobNumber";
  public static final String COMPANY = "company";
  public static final String PARTNERS = "partners";
  public static final String RECRUITERS = "recruiters";
  public static final String RESEARCHERS = "researchers";
  public static final String EAS = "eas";
  public static final String CLIENT_TEAM = "clienTeam";
  public static final String EMAIL = "email";
  public static final String TITLE = "title";
  public static final String COUNTRY = "country";
  public static final String LINKEDIN_URL = "linkedinUrl";
  public static final String BIO = "bio";
  public static final String CURRENT_JOB_TITLE = "currentJobTitle";
  public static final String MOBILE_PHONE = "mobilePhone";
  public static final String WORK_PHONE = "workPhone";
  public static final String PUBLISHED_BIO = "publishedBio";
  public static final String WORK_EMAIL = "workEmail";
  public static final String RESUME = "Resume";
  public static final String COMMENTS = "comments";
  public static final String CREATED_BY = "createdBy";
  public static final String CREATED_AT = "createdAt";
  public static final String UPDATED_AT = "updatedAt";
  public static final String REPLY = "reply";
  public static final String REPLIES = "replies";
  public static final String CANDIDATE_ID = "candidateId";



  // google capcha
  public static final String GOOGLE_CAPTCHA_SECRET_KEY = "6LfPQ10aAAAAAHP7HDxskU_c1c8oBVSpf5SZdZ4C";
  public static final String GOOGLE_CAPTCHA_SITE_KEY = "6LfPQ10aAAAAAHvVgZtpCU5ROuzwup5Q6yyZMoq_";
  public static final String GOOGLE_CAPTCHA_VALIDATE_URL =
      "https://www.google.com/recaptcha/api/siteverify";

  // Json Filter Name
  public static final String COMPANY_FILTER = "companyFilter";
  public static final String LOCATION_FILTER = "locationFilter";
  public static final String CONTACT_FILTER = "contactFilter";
  public static final String SEARCH_FILTER = "searchFilter";
  public static final String CANDIDATE_FILTER = "candidateFilter";
  public static final String CANDIDATE_FEEDBACK_FILTER = "candidateFeedbackFilter";
  public static final String CANDIDATE_FEEDBACK_REPLY_FILTER = "candidateFeedbackReplyFilter";

  // messages
  public static final String DATA_SAVED = "Data saved successfully";
  public static final String DATA_ALREADY_INITIALIZED = "Data already initialized";

  // date formatter
  public static final String GALAXY_DATE_AND_TIME_FORMATTER = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
  public static final String FEEDBACK_DATE_FORMATTER = "yyyy-MM-dd";

  // Test Fields
  public static final String TEST = "Test";
  public static final String STRING = "string";

  // Swagger User authentication API desc
  public static final String USER_AUTHENTICATION_DESC =
      "To authentication user select grant_type = password and enter username, password parameters.\r\n"
          + "\r\n"
          + "To get a new authorization token after expiration select grant_type = refereshtoken and enter the current valid refresh token.";

  public static final String ATHENA_REPORT_URL =
      "/delegate/NewReportServlet?params={%22pageSize%22:%22{pageSize}%22,%22locale%22:%22{locale}%22,%22contactId%22:%22{contactId}%22,%22newReportAction%22:%22AthenaIndidualConsultingReport%22}";
}
