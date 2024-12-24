package com.mir.weather.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mir.weather.dto.UserDto;
import com.mir.weather.entity.UserDoc;
import com.mir.weather.exception.WeatherAppException;
import com.mir.weather.repository.UserRepository;
import com.mir.weather.util.UserUtil;
import java.net.http.HttpClient;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class WeatherBeanConfigTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private AuthenticationConfiguration authenticationConfiguration;

  @InjectMocks
  private WeatherBeanConfig weatherBeanConfig;

  private static UserDoc getUserEntity() {
    UserDoc entity = new UserDoc();
    entity.setPassword("password");
    entity.setId(UUID.randomUUID());
    entity.setActive(true);
    entity.setUsername("username");
    return entity;
  }

  @BeforeEach
  void setUp() {
    weatherBeanConfig = new WeatherBeanConfig(userRepository);
  }

  @Test
  void testUserDetailsService() {
    String username = "testUser";
    UserDoc entity = getUserEntity();

    when(userRepository.findByUsername(username)).thenReturn(
        Optional.of(entity));

    UserDetailsService userDetailsService = weatherBeanConfig.userDetailsService();
    assertEquals(new UserDto(entity),
        userDetailsService.loadUserByUsername(username));

    verify(userRepository, times(1)).findByUsername(username);
  }

  @Test
  void testUserDetailsServiceUserNotFound() {
    String username = "nonExistingUser";
    when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

    UserDetailsService userDetailsService = weatherBeanConfig.userDetailsService();
    assertThrows(WeatherAppException.class,
        () -> userDetailsService.loadUserByUsername(username));

    verify(userRepository, times(1)).findByUsername(username);
  }

  @Test
  void testPasswordEncoder() {
    PasswordEncoder passwordEncoder = weatherBeanConfig.passwordEncoder();

    String rawPassword = "password";
    String encodedPassword = passwordEncoder.encode(rawPassword);

    assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
  }

  @Test
  void testAuthenticationManager() throws Exception {
    AuthenticationManager authenticationManager = mock(
        AuthenticationManager.class);
    when(authenticationConfiguration.getAuthenticationManager()).thenReturn(
        authenticationManager);

    AuthenticationManager result = weatherBeanConfig.authenticationManager(
        authenticationConfiguration);
    assertEquals(authenticationManager, result);
  }

  @Test
  void testAuthenticationProvider() {
    DaoAuthenticationProvider authProvider = (DaoAuthenticationProvider) weatherBeanConfig.authenticationProvider();

    assertNotNull(authProvider);
    assertNotNull(weatherBeanConfig.passwordEncoder());
    assertNotNull(weatherBeanConfig.userDetailsService());
  }

  @Test
  void testObjectMapper() {
    ObjectMapper objectMapper = weatherBeanConfig.objectMapper();
    assertNotNull(objectMapper);
  }

  @Test
  void testUserUtil() {
    UserUtil userUtil = weatherBeanConfig.util();
    assertNotNull(userUtil);
  }

  @Test
  void testHttpClient() {
    HttpClient httpClient = weatherBeanConfig.httpClient();
    assertNotNull(httpClient);
  }


}
