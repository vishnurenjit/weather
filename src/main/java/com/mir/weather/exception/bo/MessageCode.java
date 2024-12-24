package com.mir.weather.exception.bo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageCode {

  USER_NOT_FOUND(1001, "User not found"),
  INVALID_ZIP(1002, "Invalid zip format"),
  INACTIVE_USER(1003, "User is not active");

  private final int code;
  private final String message;
}
