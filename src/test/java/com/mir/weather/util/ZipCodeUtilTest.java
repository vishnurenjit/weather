package com.mir.weather.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ZipCodeUtilTest {

  @Test
  void testIsValidZipCode() {
    assertTrue(ZipCodeUtil.isValidZipCode("12345"));
    assertTrue(ZipCodeUtil.isValidZipCode("12345-6789"));
    assertFalse(ZipCodeUtil.isValidZipCode("1234"));
    assertFalse(ZipCodeUtil.isValidZipCode("123456"));
    assertFalse(ZipCodeUtil.isValidZipCode("1234a"));
    assertFalse(ZipCodeUtil.isValidZipCode(""));
    assertFalse(ZipCodeUtil.isValidZipCode(null));
  }

  @Test
  void testGet5DigZip() {
    assertEquals(12345, ZipCodeUtil.get5DigZip("12345"));
    assertEquals(12345, ZipCodeUtil.get5DigZip("12345-6789"));
    assertThrows(NumberFormatException.class,
        () -> ZipCodeUtil.get5DigZip("1234a"));
    assertThrows(StringIndexOutOfBoundsException.class,
        () -> ZipCodeUtil.get5DigZip("1234"));
  }
}
