package com.mir.weather.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mir.weather.repository.UserRepository;
import com.mir.weather.util.UserUtil;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserUtil userUtil;

  private UserService userService;

  @BeforeEach
  public void setup() {
    userService = new UserService(userRepository, userUtil);
  }


  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void setActive(boolean active) {
    UUID userId = UUID.randomUUID();
    when(userUtil.getCurUserId()).thenReturn(userId);

    userService.setActive(active);

    ArgumentCaptor<Boolean> captor = ArgumentCaptor.forClass(Boolean.class);
    verify(userRepository).findAndSetActiveById(eq(userId), captor.capture());
    assertEquals(captor.getValue(), active);
  }
}