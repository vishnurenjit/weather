package com.mir.weather.service;

import static com.mir.weather.exception.bo.MessageCode.INACTIVE_USER;
import static com.mir.weather.exception.bo.MessageCode.INVALID_ZIP;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mir.weather.client.WeatherClient;
import com.mir.weather.client.dto.WeatherData;
import com.mir.weather.dto.WeatherDto;
import com.mir.weather.entity.WeatherDoc;
import com.mir.weather.exception.WeatherAppException;
import com.mir.weather.mapper.WeatherMapper;
import com.mir.weather.repository.WeatherRepository;
import com.mir.weather.util.UserUtil;
import com.mir.weather.util.ZipCodeUtil;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class WeatherServiceTest {

  @Mock
  private WeatherClient client;

  @Mock
  private WeatherRepository repository;

  @Mock
  private UserUtil userUtil;

  @Mock
  private WeatherMapper mapper;

  @InjectMocks
  private WeatherService weatherService;

  private String validZip;
  private String invalidZip;
  private WeatherData weatherData;
  private WeatherDto weatherDto;
  private UUID userId;

  @BeforeEach
  void setUp() {
    validZip = "12345";
    invalidZip = "123";
    weatherData = mock(WeatherData.class);
    weatherDto = mock(WeatherDto.class);
    userId = UUID.randomUUID();
  }

  @Test
  void testGetWeatherDataForZip_ValidZip() throws IOException, InterruptedException {
    // Setup

    when(userUtil.isCurUserActive()).thenReturn(true);
    when(client.getWeatherData(validZip)).thenReturn(weatherData);
    when(mapper.mapFromDomain(weatherData)).thenReturn(weatherDto);

    // Execute
    WeatherDto result = weatherService.getWeatherDataForZip(validZip);

    // Verify
    assertEquals(weatherDto, result);
    verify(client, times(1)).getWeatherData(validZip);
    verify(mapper, times(1)).mapFromDomain(weatherData);
    verify(repository, times(1)).save(any(WeatherDoc.class));
  }

  @Test
  void testGetWeatherDataForZip_InvalidZip()
      throws IOException, InterruptedException {

    // Execute & Verify
    WeatherAppException exception = assertThrows(WeatherAppException.class,
        () -> weatherService.getWeatherDataForZip(invalidZip));

    assertEquals(INVALID_ZIP.getCode(), exception.getErrorCode());
    verify(client, never()).getWeatherData(invalidZip);
    verify(mapper, never()).mapFromDomain(weatherData);
    verify(repository, never()).save(any(WeatherDoc.class));
  }

  @Test
  void testGetWeatherDataForZip_InactiveUser()
      throws IOException, InterruptedException {
    // Setup
    when(userUtil.isCurUserActive()).thenReturn(false);

    // Execute & Verify
    WeatherAppException exception = assertThrows(WeatherAppException.class,
        () -> weatherService.getWeatherDataForZip(validZip));

    assertEquals(INACTIVE_USER.getCode(), exception.getErrorCode());
    verify(client, never()).getWeatherData(validZip);
    verify(mapper, never()).mapFromDomain(weatherData);
    verify(repository, never()).save(any(WeatherDoc.class));
  }

  @Test
  void testGetUserHistory() {
    // Setup
    when(userUtil.getCurUserId()).thenReturn(userId);
    WeatherDoc weatherDoc = mock(WeatherDoc.class);
    when(repository.findByUserId(userId)).thenReturn(List.of(weatherDoc));
    when(weatherDoc.getWeatherResponse()).thenReturn(weatherData);
    when(mapper.mapFromDomain(weatherData)).thenReturn(weatherDto);

    // Execute
    List<WeatherDto> result = weatherService.getUserHistory();

    // Verify
    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertEquals(weatherDto, result.get(0));
    verify(repository, times(1)).findByUserId(userId);
    verify(mapper, times(1)).mapFromDomain(weatherData);
  }

  @Test
  void testDeleteUserHistory() {
    // Setup
    when(userUtil.getCurUserId()).thenReturn(userId);

    // Execute
    weatherService.deleteUserHistory();

    // Verify
    verify(repository, times(1)).deleteByUserId(userId);
  }
}
