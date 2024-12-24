package com.mir.weather.config.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("weather.client")
public record WeatherClientProperties(String key, String url) {

}