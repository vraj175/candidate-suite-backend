package com.aspire.kgp.constant;

public class Constant {

	private Constant() {
	}

	public static final String ACCESS_TOKEN_X_TOKEN = "X-Token";
	public static final String X_API_KEY = "X-API-Key";
	public static final String TIMESTAMP = "timestamp";
	public static final String STATUS = "status";
	public static final String ERROR = "error";
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

	public static final String CLIENT_SUITE_PROD_SERVER_URL = "https://clients.kingsleygate.com";
	public static final String PARSING_ERROR = "Exception while parsing";
	public static final String ILLEGAL_ARGUMENT_EXCEPTION = "Illegal Argument Exception";
	public static final String JSON_PROCESSING_EXCEPTION = "JsonProcessingException : ";
	public static final String INVALID_CANDIDATE_ID = "Invalid Candidate Id";

	public static final String EMPTY_STRING = "";
	public static final String ASC = "ASC";
	public static final String DESC = "DESC";
	// Api url
	public static final String USER_PROFILE_URL = "/users/profile";
	public static final String CONDIDATE_URL = "/candidates/{candidateId}";
	public static final String CONTACT_URL = "/contacts/{contactId}";
	public static final String USER_URL = "/users/{userId}";
	public static final String COMPANY_LIST = "/clientsuite/partner/companies?stage={STAGE}";
	public static final String SEARCHES_LIST_BY_COMAPNY = "/clientsuite/partner/companies/{companyId}/searches";
	public static final String SEARCHES_LIST_BY_IDS = "/searches/search-details-by-ids";
	public static final String CANDIDATE_LIST_URL = "/searches/{searchId}/candidates";
	public static final String CANDIDATE_URL = "/candidates/{candidateId}/activity";
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

	// Json Fields
	public static final String FIRST_NAME = "firstName";
	public static final String LAST_NAME = "lastName";
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
	public static final String PUBLISHED_BIO = "publishedBio";

}
