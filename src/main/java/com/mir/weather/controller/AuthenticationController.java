package com.mir.weather.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mir.weather.dto.LoginResponse;
import com.mir.weather.dto.LoginUser;
import com.mir.weather.dto.RegisterUser;
import com.mir.weather.dto.User;
import com.mir.weather.service.AuthenticationService;
import com.mir.weather.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {

  private final JwtService jwtService;

  private final AuthenticationService authenticationService;

  public AuthenticationController(JwtService jwtService,
      AuthenticationService authenticationService) {
    this.jwtService = jwtService;
    this.authenticationService = authenticationService;
  }

  @PostMapping("/signup")
  public ResponseEntity register(@RequestBody RegisterUser registerUserDto) {

    authenticationService.signup(registerUserDto);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PostMapping("/login")
  public LoginResponse authenticate(@RequestBody LoginUser loginUserDto)
      throws JsonProcessingException {
    User authenticatedUser = authenticationService.authenticate(
        loginUserDto);

    String jwtToken = jwtService.generateToken(authenticatedUser);

    return new LoginResponse(jwtToken, jwtService.getExpirationTime());
  }
}