package com.mir.weather.dto;

import lombok.Builder;
import lombok.Data;


/**
 * Response for get weather API
 */

@Builder
@Data
public class WeatherDto {

  private Temperature temperature;

  private int pressure;

  private int humidity;

  private Wind wind;

  public record Temperature(float temp, float feelsLike, float temMin,
                            float tempMax) {

  }

  public record Wind(float speed, int deg, float gust) {

  }
}
