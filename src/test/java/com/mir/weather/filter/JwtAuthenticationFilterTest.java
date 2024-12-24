package com.mir.weather.filter;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mir.weather.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.servlet.HandlerExceptionResolver;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationFilterTest {

  @Mock
  private JwtService jwtService;

  @Mock
  private UserDetailsService userDetailsService;

  @Mock
  private HandlerExceptionResolver handlerExceptionResolver;

  @InjectMocks
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  private MockHttpServletRequest request;
  private MockHttpServletResponse response;
  private FilterChain filterChain;

  @BeforeEach
  void setUp() {
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
    filterChain = mock(FilterChain.class);
  }

  @Test
  void testDoFilterInternal_noAuthHeader()
      throws ServletException, IOException {
    jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

    verify(filterChain, times(1)).doFilter(request, response);
  }

  @Test
  void testDoFilterInternal_invalidAuthHeader()
      throws ServletException, IOException {
    request.addHeader("Authorization", "InvalidToken");

    jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

    verify(filterChain, times(1)).doFilter(request, response);
  }

  @Test
  void testDoFilterInternal_validAuthHeader()
      throws ServletException, IOException {
    String token = "valid.jwt.token";
    String username = "user";
    request.addHeader("Authorization", "Bearer " + token);

    UserDetails userDetails = mock(UserDetails.class);
    when(jwtService.extractUsername(token)).thenReturn(username);
    when(userDetailsService.loadUserByUsername(username)).thenReturn(
        userDetails);
    when(jwtService.isTokenExpired(token)).thenReturn(false);

    jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

    verify(jwtService, times(1)).extractUsername(token);
    verify(userDetailsService, times(1)).loadUserByUsername(username);
    verify(jwtService, times(1)).isTokenExpired(token);
    verify(filterChain, times(1)).doFilter(request, response);
  }

  @Test
  void testDoFilterInternal_exceptionHandling()
      throws ServletException, IOException {
    request.addHeader("Authorization", "Bearer invalid.token");

    doThrow(new RuntimeException("Test exception")).when(jwtService)
        .extractUsername(anyString());

    jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

    verify(handlerExceptionResolver, times(1)).resolveException(eq(request),
        eq(response), isNull(), any(Exception.class));
  }
}
