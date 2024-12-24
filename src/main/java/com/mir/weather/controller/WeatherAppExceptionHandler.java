package com.mir.weather.controller;

import com.mir.weather.dto.Status;
import com.mir.weather.dto.WeatherAppResource;
import com.mir.weather.exception.WeatherAppException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class WeatherAppExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value = {WeatherAppException.class})
  protected ResponseEntity handleWeatherAppException(WeatherAppException ex) {
    return getWeatherAppResourceResponseEntity(Status.buildErrorStatus(ex));
  }

  @ExceptionHandler(value = {Exception.class})
  protected ResponseEntity handleGeneralException(Exception ex) {
    return getWeatherAppResourceResponseEntity(
        Status.buildErrorStatus(ex.getMessage()));
  }

  private static ResponseEntity<WeatherAppResource> getWeatherAppResourceResponseEntity(
      Status status) {
    WeatherAppResource resource = new WeatherAppResource(null,
        List.of(status));
    return ResponseEntity.status(HttpStatus.CONFLICT).body(resource);
  }
}
