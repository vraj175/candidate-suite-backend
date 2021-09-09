package com.aspire.kgp.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.ContactDTO;
import com.aspire.kgp.exception.APIException;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;


@Component
public class ContactUtil {
  static Log log = LogFactory.getLog(ContactUtil.class.getName());

  @Autowired
  RestUtil restUtil;

  public final ContactDTO getContactDetails(String contactId) {
    String apiResponse =
        restUtil.newGetMethod(Constant.CONTACT_URL.replace("{contactId}", contactId));

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
        .newGetImage(Constant.CONTACT_PROFILE_IMAGE_URL.replace("{CONTACTID}", contactId));
  }

  public String uploadCandidateResume(MultipartFile multipartFile, String contactId) {
    JsonObject paramJSON = new JsonObject();
    paramJSON.addProperty("description", "");
    paramJSON.addProperty("show_in_clientsuite", false);
    
    File file;
    try {
      String fileName = multipartFile.getOriginalFilename();
      if(fileName==null) {
        throw new APIException(Constant.FILE_UPLOAD_ERROR);
      }
      String extension = fileName.substring(fileName.lastIndexOf("."));
      log.info(extension);
      file = File.createTempFile(fileName.substring(0,fileName.lastIndexOf(".")-1), extension);
      FileOutputStream fos = new FileOutputStream(file); 
      fos.write(multipartFile.getBytes());
      fos.close();
    } catch (IOException e1) {
      e1.printStackTrace();
      throw new APIException(Constant.FILE_UPLOAD_ERROR);
    }
    
    String response = restUtil.postMethod(
        Constant.UPLOAD_RESUME_URL.replace("{CONTACTID}", contactId), paramJSON.toString(), file);
    log.info(response);
    JsonObject responseJson = new Gson().fromJson(response, JsonObject.class);

    try {
      if (responseJson.get("id").getAsString() != null) {
        return Constant.FILE_UPLOADED_SUCCESSFULLY;
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new APIException(Constant.FILE_UPLOAD_ERROR);
    }
    return Constant.FILE_UPLOADED_SUCCESSFULLY;
  }

}
