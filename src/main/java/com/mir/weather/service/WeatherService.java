package com.mir.weather.service;

import static com.mir.weather.exception.bo.MessageCode.*;

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
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

/**
 * Service class for weather-related operations.
 */
@Service
public class WeatherService {

  private final WeatherClient client;
  private final WeatherRepository repository;
  private final UserUtil userUtil;
  private final WeatherMapper mapper;

  /**
   * Constructor for WeatherService.
   *
   * @param client     the WeatherClient
   * @param repository the WeatherRepository
   * @param userUtil   the UserUtil
   * @param mapper     the WeatherMapper
   */
  public WeatherService(WeatherClient client, WeatherRepository repository,
      UserUtil userUtil, WeatherMapper mapper) {
    this.client = client;
    this.repository = repository;
    this.userUtil = userUtil;
    this.mapper = mapper;
  }

  /**
   * Retrieves weather data for a given ZIP code.
   *
   * @param zip the ZIP code
   * @return the WeatherDto containing weather information
   * @throws IOException          if an I/O exception occurs
   * @throws InterruptedException if the operation is interrupted
   */
  public WeatherDto getWeatherDataForZip(String zip)
      throws IOException, InterruptedException {

    // Validate the provided ZIP code
    Optional.of(zip).filter(ZipCodeUtil::isValidZipCode)
        .orElseThrow(() -> new WeatherAppException(INVALID_ZIP));

    // Check if the current user is active, throw an exception if not
    if (!userUtil.isCurUserActive()) {
      throw new WeatherAppException(INACTIVE_USER);
    }

    // Fetch the weather data for the given ZIP code using the WeatherClient
    WeatherData response = client.getWeatherData(zip);

    // Save the fetched weather data to the database
    saveToDB(response, zip);

    // Map the WeatherData to WeatherDto and return the result
    return mapper.mapFromDomain(response);
  }


  /**
   * Saves weather data to the database.
   *
   * @param response the WeatherData to save
   * @param zip      the ZIP code
   */
  private void saveToDB(WeatherData response, String zip) {
    WeatherDoc weather = new WeatherDoc();
    weather.setId(UUID.randomUUID());
    weather.setUserId(userUtil.getCurUserId());
    weather.setWeatherResponse(response);
    weather.setZip(ZipCodeUtil.get5DigZip(zip));
    weather.setCreatedAt(LocalDateTime.now());
    repository.save(weather);
  }

  /**
   * Retrieves the weather history for the current user.
   *
   * @return a list of WeatherDto containing the user's weather history
   */
  public List<WeatherDto> getUserHistory() {
    return repository.findByUserId(userUtil.getCurUserId()).stream()
        .map(WeatherDoc::getWeatherResponse).map(mapper::mapFromDomain)
        .toList();
  }

  /**
   * Deletes the weather history for the current user.
   */
  public void deleteUserHistory() {
    repository.deleteByUserId(userUtil.getCurUserId());
  }
}
