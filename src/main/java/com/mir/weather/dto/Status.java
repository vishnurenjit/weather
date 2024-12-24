package com.mir.weather.dto;

import com.mir.weather.exception.WeatherAppException;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Status {

  private String code;
  private String message;
  private Type type;

  public static Status buildErrorStatus(String message) {
    return Status.builder().message(message).type(Type.ERROR)
        .code("generic_error").build();
  }

  public static Status buildErrorStatus(WeatherAppException ex) {
    return Status.builder().message(ex.getMessage()).type(Type.ERROR)
        .code(String.valueOf(ex.getErrorCode())).build();
  }

  private enum Type {
    INFO, ERROR
  }
}
