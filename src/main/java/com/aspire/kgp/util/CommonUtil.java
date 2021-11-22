package com.aspire.kgp.util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.crypto.bcrypt.BCrypt;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.UserDTO;

public class CommonUtil {
  private static final Log log = LogFactory.getLog(CommonUtil.class);

  private CommonUtil() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Method for replace ' with \' for sales-force query Note: In Query use replace instead of
   * replaceAll for replace ' with \'. In replaceAll it will not work because replace All is using
   * regex
   * 
   * @param str
   * @return
   */
  public static String replaceQuotes(String str) {
    if ("".equals(str) || str == null)
      return str;
    return str.replace("'", "\\\\'");
  }

  /**
   * Method will return true if response is null or empty
   * 
   * @param response
   * @return
   */
  public static boolean checkNullString(String response) {
    return (response == null) || (response).equals("");
  }

  /**
   * Method will return true if response is not null or not empty
   * 
   * @param response
   * @return
   */
  public static boolean checkNotNullString(String response) {
    return response != null && !response.trim().isEmpty();
  }

  /**
   * Method to convert date to report format
   * 
   * @param date
   * @return
   * @throws java.text.ParseException
   */
  public static String convertDateForReport(String date) throws java.text.ParseException {
    SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat newFormat = new SimpleDateFormat("MMM dd, yyyy");
    if (date != null && !date.isEmpty()) {
      log.info(newFormat.format(oldFormat.parse(date)));
      return newFormat.format(oldFormat.parse(date));
    }
    return date;
  }

  public static void invokeSetter(Object obj, String variableName, Object variableValue) {
    try {
      /**
       * Get object of PropertyDescriptor using variable name and class Note: To use
       * PropertyDescriptor on any field/variable, the field must have both `Setter` and `Getter`
       * method.
       */
      log.debug("variableName : " + variableName);
      PropertyDescriptor objPropertyDescriptor =
          new PropertyDescriptor(variableName, obj.getClass());

      /**
       * Get field/variable value using getReadMethod() variableValue is Object because value can be
       * an Object, Integer, String, etc... log.info("not need to authorize with api key ");
       * 
       */
      objPropertyDescriptor.getWriteMethod().invoke(obj, variableValue);
      /* Print value of variable */

    } catch (IllegalAccessException | IllegalArgumentException e) {
      log.info(Constant.ILLEGAL_ARGUMENT_EXCEPTION + " : " + e);
    } catch (InvocationTargetException e) {
      log.info("InvocationTargetException : " + e);
    } catch (IntrospectionException e) {
      log.info("IntrospectionException : " + e);
    }
  }

  public static double round(double value, int precision) {
    int scale = (int) Math.pow(10, precision);
    try {
      return (double) Math.round(value * scale) / scale;
    } catch (Exception ex) {
      log.error("invalid number");
      return 0.0;
    }
  }

  public static String fetchCurrencySymbol(String currencyIsoCode) {
    if (checkNullString(currencyIsoCode)) {
      return "$";
    }
    switch (currencyIsoCode) {
      case "INR":
        return "₹";
      case "EUR":
        return "€";
      case "SGD":
        return "S$";
      case "GBP":
        return "£";
      case "BRL":
        return "R$";
      case "CRC":
        return "CRC";
      case "PAB":
        return "B/.";
      case "PEN":
        return "S/.";
      case "VEF":
        return "Bs";
      case "MXN":
        return "Mex$";
      case "RUR":
        return "₽";
      default:
        return "$";
    }
  }

  public static Object invokeGetter(Object obj, String variableName) {
    try {
      /**
       * Get object of PropertyDescriptor using variable name and class Note: To use
       * PropertyDescriptor on any field/variable, the field must have both `Setter` and `Getter`
       * method.
       */
      log.info("variableName : " + variableName);
      PropertyDescriptor objPropertyDescriptor =
          new PropertyDescriptor(variableName, obj.getClass());
      /**
       * Get field/variable value using getReadMethod() variableValue is Object because value can be
       * an Object, Integer, String, etc...
       */
      /* Print value of variable */
      return objPropertyDescriptor.getReadMethod().invoke(obj);
    } catch (IllegalAccessException | IllegalArgumentException e) {
      log.info(Constant.ILLEGAL_ARGUMENT_EXCEPTION + " : " + e);
    } catch (InvocationTargetException e) {
      log.info("InvocationTargetException : " + e);
    } catch (IntrospectionException e) {
      log.info("IntrospectionException : " + e);
    }
    return null;
  }

  public static String convertDoubeToInt(double value) {
    int val = 0;
    try {
      val = (int) value;
      if (value - val > 0)
        return String.valueOf(value);
      else
        return String.valueOf(val);
    } catch (Exception e) {
      return "0";
    }
  }

  public static String getOnePointDecimal(String str) {
    DecimalFormat df = new DecimalFormat("#.#");
    if (str != null && !str.isEmpty() && NumberUtils.isNumber(str)) {
      return df.format(Double.valueOf(str));
    }
    return str;
  }

  public static boolean isNumeric(String str) {
    NumberFormat formatter = NumberFormat.getInstance();
    ParsePosition pos = new ParsePosition(0);
    formatter.parse(str, pos);
    return str.length() == pos.getIndex();
  }

  /***
   * 
   * @param reportParams
   * @param request
   * @return
   */
  public static String getServerUrl(HttpServletRequest request) {
    String host = Constant.STRING_LOCALHOST;
    try {
      StringBuffer url = request.getRequestURL();
      String uri = request.getRequestURI();
      host = url.substring(0, url.indexOf(uri));
      if (!host.contains(Constant.STRING_LOCALHOST)) {
        host = host.replace(Constant.STRING_HTTP, Constant.STRING_HTTPS);
      }
      log.info("host :" + host);
    } catch (Exception e) {
      log.error("Exception:: " + e);
    }
    return host;
  }

  public static String hash(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt(10));
  }

  public static boolean verifyHash(String password, String hash) {
    return BCrypt.checkpw(password, hash);
  }

  public static String getLanguageCode(String language) {
    switch (language) {
      case Constant.ENGLISH:
        return Constant.ENGLISH_CODE;
      case Constant.SPANISH:
        return Constant.SPANISH_CODE;
      case Constant.PORTUGUESE:
        return Constant.PORTUGUESE_CODE;
      default:
        return Constant.ENGLISH_CODE;
    }
  }

  /**
   * Method to convert date to desired format
   * 
   * @param date
   * @return
   * @throws java.text.ParseException
   */

  public static String dateFormatter(String interviewDate) throws ParseException {
    SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    Date date = oldFormat.parse(interviewDate);
    SimpleDateFormat newFormat = new SimpleDateFormat("dd MMM yyyy 'at' hh:mmaa z");
    interviewDate = newFormat.format(date);
    interviewDate = interviewDate.replace("AM", "am").replace("PM", "pm")
        .replace(interviewDate.substring(3, 6), interviewDate.substring(3, 6).toUpperCase());
    return (interviewDate.substring(0, 2)
        + getDayOfMonthSuffix(Integer.parseInt(interviewDate.substring(0, 2))))
        + interviewDate.substring(2);
  }

  /**
   * Method to add suffix in date
   * 
   * @param date
   * @return
   */

  private static String getDayOfMonthSuffix(final int n) {
    checkArgument(n >= 1 && n <= 31, "illegal day of month: " + n);
    if (n >= 11 && n <= 13) {
      return "th";
    }
    switch (n % 10) {
      case 1:
        return "st";
      case 2:
        return "nd";
      case 3:
        return "rd";
      default:
        return "th";
    }
  }

  public static String removeTime(String date) {
    if (CommonUtil.checkNotNullString(date) && date.contains("T")) {
      String[] spliteddate = date.split("T");
      date = spliteddate[0];
    }
    return date;
  }

  public static Set<String> teamMemberList(List<UserDTO> users, Set<String> partnerEmailList) {
    log.info("Creating Team member email and name set");
    for (UserDTO user : users) {
      if (user != null && CommonUtil.checkNotNullString(user.getId())) {
        partnerEmailList.add(user.getEmail() + "##" + user.getName());
      }
    }
    return partnerEmailList;
  }

  public static Set<String> teamPartnerMemberList(List<UserDTO> users,
      Set<String> partnerEmailList) {
    log.info("Creating Team member email and name set");
    for (UserDTO user : users) {
      if (user != null && CommonUtil.checkNotNullString(user.getId())
          && user.getExecutionCredit() != null) {
        partnerEmailList.add(user.getEmail() + "##" + user.getName());
      }
    }
    return partnerEmailList;
  }

  public static boolean isJSONValid(String jsonString) {
    try {
      new JSONObject(jsonString);
    } catch (JSONException ex) {
      return false;
    }
    return true;
  }
}
