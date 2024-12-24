package com.mir.weather.mapper;

import com.mir.weather.client.dto.Main;
import com.mir.weather.client.dto.WeatherData;
import com.mir.weather.client.dto.WeatherData.Wind;
import com.mir.weather.dto.WeatherDto;
import com.mir.weather.dto.WeatherDto.Temperature;
import org.springframework.stereotype.Component;

/**
 * Mapper class for converting WeatherData domain objects to WeatherDto objects.
 */
@Component
public class WeatherMapper {

  /**
   * Maps WeatherData to WeatherDto.
   *
   * @param data the WeatherData to map from
   * @return the mapped WeatherDto
   */
  public WeatherDto mapFromDomain(WeatherData data) {
    Main main = data.main();
    return WeatherDto.builder().temperature(mapToTemperature(main))
        .wind(mapFromDomain(data.wind())).humidity(main.humidity())
        .pressure(main.pressure()).build();
  }

  /**
   * Maps Main object to Temperature object.
   *
   * @param main the Main object to map from
   * @return the mapped Temperature object
   */
  private Temperature mapToTemperature(Main main) {
    return new Temperature(main.temp(), main.feels_like(), main.temp_min(),
        main.temp_max());
  }

  /**
   * Maps Wind object to WeatherDto.Wind object.
   *
   * @param wind the Wind object to map from
   * @return the mapped WeatherDto.Wind object
   */
  private WeatherDto.Wind mapFromDomain(Wind wind) {
    return new WeatherDto.Wind(wind.speed(), wind.deg(), wind.gust());
  }
}
