package com.mir.weather.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mir.weather.config.dto.JwtProperties;
import com.mir.weather.dto.User;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

  private static final String KEY = "3cfa76ef14937c1c0ea519f8fc057a80fcd04a7420f8e8bcd0a7567c272e007b";
  @Mock
  private User user;
  private final JwtService jwtService;

  public JwtServiceTest() {
    this.jwtService = new JwtService(new JwtProperties(KEY, 3600000));
  }

  @Test
  void testGenerateTokenAndExtractUsername() throws JsonProcessingException {

    when(user.getUsername()).thenReturn("Sample_username");
    String token = jwtService.generateToken(user);
    assertEquals("Sample_username", jwtService.extractUsername(token));
  }


  @Test
  void getExpirationTime() {

    assertEquals(3600000, jwtService.getExpirationTime());
  }

  @Test
  void isTokenExpiredIsFalseIfTokenNotExpired() throws JsonProcessingException {

    assertFalse(jwtService.isTokenExpired(jwtService.generateToken(user)));
  }

  @Test
  void isTokenExpiredIsThrowExceptionIfTokenExpired()
      throws InterruptedException {

    JwtService jwtServiceTest = new JwtService(new JwtProperties(KEY, 1));
    Thread.sleep(2);
    assertThrows(ExpiredJwtException.class,
        () -> jwtService.isTokenExpired(jwtServiceTest.generateToken(user)));
  }
}