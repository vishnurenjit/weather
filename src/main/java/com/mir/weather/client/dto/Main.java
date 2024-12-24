package com.mir.weather.client.dto;

public record Main(
    float temp,
    float feels_like,
    float temp_min,
    float temp_max,
    int pressure,
    int humidity,
    int sea_level,
    int grnd_level
) {

}