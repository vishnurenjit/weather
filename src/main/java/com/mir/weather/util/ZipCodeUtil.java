package com.mir.weather.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Class to handle Zipcode related logic<p/>
 */
public class ZipCodeUtil {

  private static final String ZIP_CODE_REGEX = "^[0-9]{5}(?:-[0-9]{4})?$";
  private static final Pattern ZIP_CODE_PATTERN = Pattern.compile(
      ZIP_CODE_REGEX);

  /**
   *
   * @param zipCode
   * @return whether the String is valid zip or not
   */
  public static boolean isValidZipCode(String zipCode) {
    if (zipCode == null || zipCode.isEmpty()) {
      return false;
    }
    Matcher matcher = ZIP_CODE_PATTERN.matcher(zipCode);
    return matcher.matches();
  }

  /**
   *
   * @param zip
   * @return 5 digit zip format
   */
  public static int get5DigZip(String zip) {
    if (zip.length() != 5) {
      return Integer.parseInt(zip.substring(0, 5));
    }
    return Integer.parseInt(zip);
  }
}
