package com.mir.weather.dto;

import java.util.List;

/**
 * Wrapper for API response
 *
 * @param data
 * @param status
 * @param <T>
 */
public record WeatherAppResource<T>(T data, List<Status> status) {

}
