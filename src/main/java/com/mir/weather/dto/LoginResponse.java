package com.mir.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

  private final String token;

  private final long expiresIn;
}
