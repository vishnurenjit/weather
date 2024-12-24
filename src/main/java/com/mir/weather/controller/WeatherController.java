package com.mir.weather.controller;

import com.mir.weather.dto.WeatherDto;
import com.mir.weather.service.WeatherService;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/weather")
@RestController
public class WeatherController {

  private final WeatherService weatherService;

  public WeatherController(WeatherService weatherService) {
    this.weatherService = weatherService;
  }

  @GetMapping("/{zip}")
  public WeatherDto getWeatherDto(@PathVariable String zip)
      throws URISyntaxException, IOException, InterruptedException {

    return weatherService.getWeatherDataForZip(zip);
  }

  @GetMapping("/history")
  public List<WeatherDto> getHistory() {

    return weatherService.getUserHistory();
  }

  @DeleteMapping("/history")
  public void deleteHistory() {

    weatherService.deleteUserHistory();
  }
}
