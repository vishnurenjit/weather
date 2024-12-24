package com.mir.weather.exception;


import com.mir.weather.exception.bo.MessageCode;
import lombok.Getter;

@Getter
public class WeatherAppException extends RuntimeException {

  private final int errorCode;

  public WeatherAppException(String message, int errorCode) {
    super(message);
    this.errorCode = errorCode;
  }

  public WeatherAppException(MessageCode messageCode) {
    super(messageCode.getMessage());
    this.errorCode = messageCode.getCode();
  }
}
