package com.mir.weather.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record WeatherData
    (
        Coord coord,
        List<Weather> weather,
        String base,
        Main main,
        int visibility,
        Wind wind,
        Rain rain,
        Clouds clouds,
        int dt,
        Sys sys,
        int timezone,
        int id,
        String name,
        int cod
    ) {

  private record Clouds(int all) {

  }

  private record Coord(double lon, double lat) {

  }

  private record Rain(@JsonProperty("1h") double _1h) {

  }

  private record Sys(
      int type,
      int id,
      String country,
      int sunrise,
      int sunset
  ) {

  }

  private record Weather(int id, String main, String description, String icon) {

  }

  public record Wind(float speed, int deg, float gust) {

  }
}
