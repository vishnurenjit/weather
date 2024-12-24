package com.mir.weather.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mir.weather.config.dto.JwtProperties;
import com.mir.weather.config.dto.WeatherClientProperties;
import com.mir.weather.dto.UserDto;
import com.mir.weather.exception.WeatherAppException;
import com.mir.weather.exception.bo.MessageCode;
import com.mir.weather.repository.UserRepository;
import com.mir.weather.util.UserUtil;
import java.net.http.HttpClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableConfigurationProperties({WeatherClientProperties.class,
    JwtProperties.class})
public class WeatherBeanConfig {

  private final UserRepository userRepository;

  public WeatherBeanConfig(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Bean
  UserDetailsService userDetailsService() {
    return username -> new UserDto(userRepository.findByUsername(username)
        .orElseThrow(() -> new WeatherAppException(MessageCode.USER_NOT_FOUND)));
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new PasswordEncoder() {
      @Override
      public String encode(CharSequence charSequence) {
        return charSequence.toString();
      }

      @Override
      public boolean matches(CharSequence charSequence, String s) {
        return charSequence.toString().equals(s);
      }
    };
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

    authProvider.setUserDetailsService(userDetailsService());
    authProvider.setPasswordEncoder(passwordEncoder());

    return authProvider;
  }

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

  @Bean
  public UserUtil util() {
    return new UserUtil();
  }

  @Bean
  public HttpClient httpClient() {
    return HttpClient.newHttpClient();
  }

}
