package com.mir.weather.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mir.weather.dto.WeatherDto;
import com.mir.weather.dto.WeatherDto.Temperature;
import com.mir.weather.dto.WeatherDto.Wind;
import com.mir.weather.service.WeatherService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class WeatherControllerTest {

  @Mock
  private WeatherService weatherService;

  @InjectMocks
  private WeatherController weatherController;

  private final ObjectMapper objectMapper = new ObjectMapper();

  private MockMvc mockMvc;

  private static WeatherDto getWeatherDto() {
    return WeatherDto.builder().wind(new Wind(2.06f, 200, 0.0f)).pressure(1027)
        .humidity(74).temperature(new Temperature(1.2f, -1.13f, 0.11f, 2.21f))
        .build();
  }

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(weatherController).build();
  }

  @Test
  void testGetWeatherDto() throws Exception {

    WeatherDto weatherDto = getWeatherDto();

    String zip = "12345";

    when(weatherService.getWeatherDataForZip(zip)).thenReturn(weatherDto);

    mockMvc.perform(get("/weather/{zip}", zip))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(
            weatherDto))); // Adjust the fields accordingly

    verify(weatherService, times(1)).getWeatherDataForZip(zip);
  }

  @Test
  void testGetHistory() throws Exception {
    List<WeatherDto> history = Collections.emptyList();
    when(weatherService.getUserHistory()).thenReturn(history);

    mockMvc.perform(get("/weather/history"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$").isEmpty());

    verify(weatherService, times(1)).getUserHistory();
  }

  @Test
  void testDeleteHistory() throws Exception {
    doNothing().when(weatherService).deleteUserHistory();

    mockMvc.perform(delete("/weather/history"))
        .andExpect(status().isOk());

    verify(weatherService, times(1)).deleteUserHistory();
  }
}
