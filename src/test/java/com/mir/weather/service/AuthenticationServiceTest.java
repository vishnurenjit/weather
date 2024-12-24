package com.mir.weather.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mir.weather.dto.LoginUser;
import com.mir.weather.dto.RegisterUser;
import com.mir.weather.dto.User;
import com.mir.weather.entity.UserDoc;
import com.mir.weather.mapper.UserMapper;
import com.mir.weather.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private AuthenticationManager authenticationManager;

  @Mock
  private UserMapper userMapper;

  private AuthenticationService authenticationService;

  private static LoginUser getLoginUser() {
    LoginUser user = new LoginUser();
    user.setUserName("user_some_name");
    return user;
  }

  @BeforeEach
  void setUp() {
    authenticationService = new AuthenticationService(userRepository,
        authenticationManager, userMapper);
  }

  @Test
  void signup() {
    RegisterUser registerUser = new RegisterUser();
    when(userMapper.mapToEntity(registerUser)).thenReturn(new UserDoc());

    authenticationService.signup(registerUser);

    ArgumentCaptor<UserDoc> captor = ArgumentCaptor.forClass(
        UserDoc.class);
    verify(userRepository).save(captor.capture());
    assertNotNull(captor.getValue().getId());
    assertTrue(captor.getValue().isActive());
  }

  @Test
  void authenticateIsUserReturnedIfAuthenticated() {

    LoginUser loginUser = getLoginUser();

    UserDoc userDoc = new UserDoc();
    userDoc.setActive(true);
    userDoc.setId(UUID.randomUUID());
    userDoc.setUsername(loginUser.getUserName());
    userDoc.setPassword("some_password");

    when(userRepository.findByUsername("user_some_name")).thenReturn(
        Optional.of(userDoc));
    User user = authenticationService.authenticate(loginUser);

    assertEquals(user.getId(), userDoc.getId());
    assertEquals(user.getPassword(), userDoc.getPassword());
    assertEquals(user.isActive(), userDoc.isActive());
    assertEquals(user.getUsername(), userDoc.getUsername());
  }
}