package com.mir.weather.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.mir.weather.dto.User;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
public class UserUtilTest {

  @Mock
  private SecurityContext securityContext;

  @Mock
  private Authentication authentication;

  @Mock
  private User user;

  @InjectMocks
  private UserUtil userUtil;

  @BeforeEach
  void setUp() {
    SecurityContextHolder.setContext(securityContext);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(user);
  }

  @Test
  void testGetCurUserId() {
    UUID expectedUserId = UUID.randomUUID();
    when(user.getId()).thenReturn(expectedUserId);

    UUID actualUserId = userUtil.getCurUserId();

    assertEquals(expectedUserId, actualUserId);
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void testIsCurUserActive(boolean active) {
    when(user.isActive()).thenReturn(active);

    boolean isActive = userUtil.isCurUserActive();

    assertEquals(isActive, active);
  }
}
