package com.mir.weather.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mir.weather.client.dto.WeatherData;
import com.mir.weather.config.dto.WeatherClientProperties;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WeatherClientTest {

  private static final String URL = "https://api.openweathermap.org/data/2.5/weather";
  private static final String KEY = "some_key";
  @Mock
  private HttpClient client;

  @Mock
  private ObjectMapper mapper;

  private WeatherClient weatherClient;

  @BeforeEach
  public void beforeAll() {
    weatherClient = new WeatherClient(new WeatherClientProperties(KEY, URL),
        mapper, client);
  }

  @Test
  void testGetWeatherData()
      throws IOException, InterruptedException, URISyntaxException {
    WeatherData data = new WeatherData(null, null, null, null, 0, null, null,
        null, 0, null, 0, 0, null, 0);
    when(mapper.readValue("response", WeatherData.class))
        .thenReturn(data);
    HttpResponse<String> httpResponse = mock(HttpResponse.class);

    ArgumentCaptor<HttpRequest> requestCaptor = ArgumentCaptor.forClass(
        HttpRequest.class);
    when(client.send(requestCaptor.capture(),
        eq(BodyHandlers.ofString()))).thenReturn(httpResponse);
    when(httpResponse.body()).thenReturn("response");

    assertEquals(data, weatherClient.getWeatherData("some_zip"));
    verify(client).send(requestCaptor.capture(), eq(BodyHandlers.ofString()));
    assertEquals("zip=some_zip,us&appid=some_key&units=metric",
        requestCaptor.getValue().uri().getQuery());
  }
}