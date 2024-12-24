package com.mir.weather.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mir.weather.client.dto.WeatherData;
import com.mir.weather.config.dto.WeatherClientProperties;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WeatherClient {

  private final WeatherClientProperties properties;

  private final ObjectMapper mapper;

  private final HttpClient client;

  @Autowired
  public WeatherClient(WeatherClientProperties properties,
      ObjectMapper mapper, HttpClient client) {
    this.properties = properties;
    this.mapper = mapper;
    this.client = client;
  }

  public WeatherData getWeatherData(String zip)
      throws InterruptedException, IOException {

    return mapper.readValue(getWeatherData(getUri(zip)), WeatherData.class);
  }

  private URI getUri(String zip) {
    return URI.create(properties.url() + "?zip=" + zip + ",us&appid="
        + properties.key() + "&units=metric");
  }

  private String getWeatherData(URI uri)
      throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
    return client.send(request, BodyHandlers.ofString()).body();
  }
}
