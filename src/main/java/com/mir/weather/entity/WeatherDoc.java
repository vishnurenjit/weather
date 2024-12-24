package com.mir.weather.entity;

import com.mir.weather.client.dto.WeatherData;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

/**
 * Document to store Weather information
 */
@Getter
@Setter
public class WeatherDoc {

  @Id
  private UUID id;

  private LocalDateTime createdAt;

  private UUID userId;

  private int zip;

  private WeatherData weatherResponse;

}
