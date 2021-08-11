package com.aspire.kgp.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class StaticContentsMultiLanguageUtil {

  private StaticContentsMultiLanguageUtil() {}

  public static Map<String, String> getStaticContentsMap(String languageCode,
      String propertyFileName) {
    String[] language = languageCode.split("_");
    // language[0] is language code and language[1] is country code
    Locale locale = new Locale(language[0], language[1]);
    ResourceBundle resourceBundle = ResourceBundle.getBundle(propertyFileName, locale);
    Enumeration<String> keys = resourceBundle.getKeys();
    Map<String, String> staticContentsMap = new HashMap<>();
    while (keys.hasMoreElements()) {
      String key = keys.nextElement();
      staticContentsMap.put(key, resourceBundle.getString(key));
    }
    return staticContentsMap;
  }
}
