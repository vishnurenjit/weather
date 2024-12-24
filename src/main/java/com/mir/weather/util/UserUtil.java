package com.mir.weather.util;

import com.mir.weather.dto.User;
import java.util.UUID;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utility class for retrieving user information from the security context.
 */
public class UserUtil {

  /**
   * Retrieves the current user's ID from the security context.
   *
   * @return the UUID of the current user
   */
  public UUID getCurUserId() {
    return ((User) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal()).getId();
  }

  /**
   * Checks if the current user is active by retrieving the user's details from the security context.
   *
   * @return true if the current user is active, false otherwise
   */
  public boolean isCurUserActive() {
    return ((User) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal()).isActive();
  }
}
