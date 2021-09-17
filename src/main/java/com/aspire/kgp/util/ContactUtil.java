package com.aspire.kgp.util;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.ContactDTO;
import com.aspire.kgp.dto.ContactReferencesDTO;
import com.aspire.kgp.dto.DocumentDTO;
import com.aspire.kgp.dto.SearchDTO;
import com.aspire.kgp.exception.APIException;
import com.aspire.kgp.exception.NotFoundException;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;


@Component
public class ContactUtil {
  static Log log = LogFactory.getLog(ContactUtil.class.getName());

  @Autowired
  RestUtil restUtil;

  public final ContactDTO getContactDetails(String contactId) {
    String apiResponse =
        restUtil.newGetMethod(Constant.CONTACT_URL.replace(Constant.CONTACT_ID, contactId));

    try {
      return new Gson().fromJson(apiResponse, new TypeToken<ContactDTO>() {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
      }.getType());
    } catch (JsonSyntaxException e) {
      throw new APIException(Constant.JSON_PROCESSING_EXCEPTION + e.getMessage());
    }
  }

  public final byte[] getContactImage(String contactId) {
    return restUtil
        .newGetImage(Constant.CONTACT_PROFILE_IMAGE_URL.replace(Constant.CONTACT_ID, contactId));
  }

  public final String updateContactDetails(String contactId, String contactData)
      throws UnsupportedEncodingException {
    return restUtil.putMethod(Constant.CONTACT_URL.replace("{contactId}", contactId), contactData);
  }

  public String uploadCandidateResume(MultipartFile multipartFile, String contactId) {
    JsonObject paramJSON = new JsonObject();
    paramJSON.addProperty("description", "");
    paramJSON.addProperty("show_in_clientsuite", false);

    File file;
    try {
      String fileName = multipartFile.getOriginalFilename();
      if (fileName == null) {
        throw new APIException(Constant.FILE_UPLOAD_ERROR);
      }
      String extension = fileName.substring(fileName.lastIndexOf("."));
      log.info(extension);
      file = File.createTempFile(fileName.substring(0, fileName.lastIndexOf(".") - 1), extension);
      FileOutputStream fos = new FileOutputStream(file);
      fos.write(multipartFile.getBytes());
      fos.close();
    } catch (IOException e1) {
      throw new APIException(Constant.FILE_UPLOAD_ERROR);
    }

    String response = restUtil.postMethod(
        Constant.RESUME_URL.replace(Constant.CONTACT_ID, contactId), paramJSON.toString(), file);
    log.info(response);
    JsonObject responseJson = new Gson().fromJson(response, JsonObject.class);

    try {
      if (responseJson.get("id").getAsString() != null) {
        return Constant.FILE_UPLOADED_SUCCESSFULLY;
      }
    } catch (Exception e) {
      throw new APIException(Constant.FILE_UPLOAD_ERROR);
    }
    return Constant.FILE_UPLOAD_ERROR;
  }

  public final DocumentDTO getContactResumes(String contactId) {
    List<DocumentDTO> documentList = null;
    String apiResponse =
        restUtil.newGetMethod(Constant.RESUME_URL.replace(Constant.CONTACT_ID, contactId));
    try {
      documentList = new Gson().fromJson(apiResponse, new TypeToken<List<DocumentDTO>>() {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
      }.getType());
    } catch (JsonSyntaxException e) {
      log.error("Oops! error while fetching document list details");
      documentList = Collections.emptyList();
    }
    log.info("End of getDocuments method");
    if (documentList.isEmpty()) {
      return null;
    }
    return documentList.get(0);
  }

  public void downloadDocument(String documentName, String attachmentId,
      HttpServletResponse response) {
    try {
      OutputStream os = response.getOutputStream();
      response.setContentType("application/octet-stream; charset=ISO-8859-1");

      ContentDisposition contentDisposition = ContentDisposition.builder("inline")
          .filename(documentName.replaceAll("[?:,!%#\"]", "")).build();

      response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
      // write document to output stream get data from API
      restUtil.newGetMethod(Constant.DOWNLOAD_ATTACHMENT.replace("{attachmentId}", attachmentId),
          os);
      os.flush();
      os.close();
    } catch (IOException e) {
      throw new APIException("error in download document");
    }
  }

  public final List<ContactReferencesDTO> getListOfReferences(String contactId) {
    String apiResponse =
        restUtil.newGetMethod(Constant.CONTACT_REFERENCE_URL.replace("{CONTACTID}", contactId));

    try {
      return new Gson().fromJson(apiResponse, new TypeToken<List<ContactReferencesDTO>>() {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
      }.getType());
    } catch (JsonSyntaxException e) {
      throw new APIException(Constant.JSON_PROCESSING_EXCEPTION + e.getMessage());
    }
  }

  public final List<SearchDTO> getListOfContactSearches(String contactId) {
    String apiResponse =
        restUtil.newGetMethod(Constant.CONTACT_SEARCHES_URL.replace("{CONTACTID}", contactId));
    List<SearchDTO> listSearch = new ArrayList<>();
    if (CommonUtil.checkNullString(apiResponse)) {
      log.error("Error while fetching contact search.");
      return listSearch;
    }
    JsonObject jsonObjects = (JsonObject) JsonParser.parseString(apiResponse);
    JsonArray jsonArray = jsonObjects.getAsJsonArray("data");

    if (jsonArray == null) {
      throw new NotFoundException("Invalid Contact Id");
    }
    try {
      return new Gson().fromJson(jsonArray, new TypeToken<List<SearchDTO>>() {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
      }.getType());
    } catch (JsonSyntaxException e) {
      throw new APIException(Constant.JSON_PROCESSING_EXCEPTION + e.getMessage());
    }
  }

  public final List<ContactDTO> getListOfContactByName(String contactName) {
    String apiResponse = restUtil
        .newGetMethod(Constant.GET_CONTACT_LIST_BY_NAME_URL.replace("{CONTACTNAME}", contactName));

    List<ContactDTO> listContact = new ArrayList<>();
    if (CommonUtil.checkNullString(apiResponse)) {
      log.error("Error while fetching contact.");
      return listContact;
    }
    JsonObject jsonObjects = (JsonObject) JsonParser.parseString(apiResponse);
    JsonArray jsonArray = jsonObjects.getAsJsonArray("data");

    if (jsonArray == null) {
      throw new NotFoundException("Invalid Contact Id");
    }
    try {
      return new Gson().fromJson(jsonArray, new TypeToken<List<ContactDTO>>() {

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
