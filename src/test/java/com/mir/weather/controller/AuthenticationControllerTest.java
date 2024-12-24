package com.mir.weather.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mir.weather.dto.LoginUser;
import com.mir.weather.dto.RegisterUser;
import com.mir.weather.dto.User;
import com.mir.weather.service.AuthenticationService;
import com.mir.weather.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {

  @Mock
  private JwtService jwtService;

  @Mock
  private AuthenticationService authenticationService;

  @InjectMocks
  private AuthenticationController authenticationController;

  private MockMvc mockMvc;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
  }

  @Test
  void testRegister() throws Exception {
    RegisterUser registerUser = new RegisterUser();

    doNothing().when(authenticationService).signup(registerUser);

    mockMvc.perform(post("/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerUser)))
        .andExpect(status().isCreated());

    verify(authenticationService, times(1)).signup(registerUser);
  }

  @Test
  void testAuthenticate() throws Exception {
    LoginUser loginUser = new LoginUser();
    User user = mock(User.class);
    String token = "jwt-token";
    long expirationTime = 3600;

    when(authenticationService.authenticate(eq(loginUser))).thenReturn(user);
    when(jwtService.generateToken(user)).thenReturn(token);
    when(jwtService.getExpirationTime()).thenReturn(expirationTime);

    mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginUser)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").value(token))
        .andExpect(jsonPath("$.expiresIn").value(expirationTime));

    verify(authenticationService, times(1)).authenticate(loginUser);
    verify(jwtService, times(1)).generateToken(user);
    verify(jwtService, times(1)).getExpirationTime();
  }
}
