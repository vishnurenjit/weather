package com.mir.weather.dto;

import java.util.UUID;

public interface User {

  String getUsername();

  String getPassword();

  UUID getId();

  boolean isActive();
}
