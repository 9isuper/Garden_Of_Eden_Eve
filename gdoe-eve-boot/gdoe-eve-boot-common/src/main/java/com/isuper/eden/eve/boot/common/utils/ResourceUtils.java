package com.isuper.eden.eve.boot.common.utils;

import com.isuper.eden.eve.boot.common.constant.EveSystemConstant;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author admin
 */
public class ResourceUtils {

  public static String getBundleAsMessageString(
      String baseName, Locale locale, String key, Object... arguments) {
    ResourceBundle resb = ResourceBundle.getBundle(baseName, locale);
    return MessageFormat.format(resb.getString(key), arguments);
  }

  public static String getBundleAsString(String baseName, Locale locale, String key) {
    ResourceBundle resb = ResourceBundle.getBundle(baseName, locale);
    return resb.getString(key);
  }

  public static String getBundleAsMessageStringForBaseResultCode(
      Locale locale, String key, Object... arguments) {
    ResourceBundle resb = ResourceBundle.getBundle(EveSystemConstant.ERROR_RESOURCE_PATH, locale);
    return MessageFormat.format(resb.getString(key), arguments);
  }

  public static String getBundleAsStringForBaseResultCode(Locale locale, String key) {
    ResourceBundle resb = ResourceBundle.getBundle(EveSystemConstant.ERROR_RESOURCE_PATH, locale);
    return resb.getString(key);
  }
}
