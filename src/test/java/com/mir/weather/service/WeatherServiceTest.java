package com.mir.weather.service;

import static org.mockito.Mockito.when;

import com.mir.weather.client.WeatherClient;
import com.mir.weather.mapper.WeatherMapper;
import com.mir.weather.repository.WeatherRepository;
import com.mir.weather.util.UserUtil;
import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

  @Mock
  private WeatherClient client;

  @Mock
  private WeatherRepository repository;

  @Mock
  private UserUtil userUtil;

  @Mock
  private WeatherMapper mapper;

  private WeatherService weatherService;

  @BeforeEach
  public void setUp() {
    weatherService = new WeatherService(client, repository, userUtil, mapper);
  }

  @Test
  void getWeatherDataForZipIfExceptionThrownIfUserInactive()
      throws URISyntaxException, IOException, InterruptedException {
    when(userUtil.isCurUserActive()).thenReturn(true);

    weatherService.getWeatherDataForZip("12345");
  }

  @Test
  void getWeatherDataForZip()
      throws URISyntaxException, IOException, InterruptedException {
    when(userUtil.isCurUserActive()).thenReturn(true);

    weatherService.getWeatherDataForZip("12345");
  }

  @Test
  void getUserHistory() {
  }

  @Test
  void deleteUserHistory() {
  }
}