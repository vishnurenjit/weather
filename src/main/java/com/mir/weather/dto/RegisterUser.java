package com.mir.weather.dto;

import lombok.Data;

@Data
public class RegisterUser {

  private String firstName;

  private String lastName;

  private String username;

  private String password;

  private boolean active;
}
