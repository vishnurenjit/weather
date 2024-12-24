package com.mir.weather.config;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mir.weather.filter.JwtAuthenticationFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@ExtendWith(MockitoExtension.class)
public class SecurityConfigurationTest {

  @Mock
  private AuthenticationProvider authenticationProvider;

  @Mock
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  private SecurityConfiguration securityConfiguration;

  @BeforeEach
  void setUp() {
    securityConfiguration = new SecurityConfiguration(jwtAuthenticationFilter,
        authenticationProvider);
  }

  @Test
  void testSecurityFilterChain() throws Exception {
    HttpSecurity httpSecurity = mock(HttpSecurity.class);

    when(httpSecurity.csrf(any(Customizer.class))).thenReturn(httpSecurity);
    when(httpSecurity.authorizeHttpRequests(any(Customizer.class))).thenReturn(
        httpSecurity);
    when(httpSecurity.sessionManagement(any(Customizer.class))).thenReturn(
        httpSecurity);
    when(
        httpSecurity.authenticationProvider(authenticationProvider)).thenReturn(
        httpSecurity);
    when(httpSecurity.addFilterBefore(jwtAuthenticationFilter,
        UsernamePasswordAuthenticationFilter.class)).thenReturn(httpSecurity);

    securityConfiguration.securityFilterChain(httpSecurity);

    verify(httpSecurity).csrf(any());
    verify(httpSecurity).authorizeHttpRequests(any());
    verify(httpSecurity).sessionManagement(any());
    verify(httpSecurity).authenticationProvider(authenticationProvider);
    verify(httpSecurity).addFilterBefore(jwtAuthenticationFilter,
        UsernamePasswordAuthenticationFilter.class);
  }

  @Test
  void testCorsConfigurationSource() {
    CorsConfigurationSource corsConfigurationSource = securityConfiguration.corsConfigurationSource();
    CorsConfiguration corsConfiguration = corsConfigurationSource.getCorsConfiguration(
        new MockHttpServletRequest("", "/**"));

    assert corsConfiguration != null;
    assert corsConfiguration.getAllowedOrigins()
        .contains("http://localhost:8005");
    assert corsConfiguration.getAllowedMethods().contains("GET");
    assert corsConfiguration.getAllowedMethods().contains("POST");
    assert corsConfiguration.getAllowedHeaders().contains("Authorization");
    assert corsConfiguration.getAllowedHeaders().contains("Content-Type");
  }
}
