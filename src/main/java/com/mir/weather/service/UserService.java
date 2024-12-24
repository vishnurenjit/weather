package com.mir.weather.service;

import com.mir.weather.repository.UserRepository;
import com.mir.weather.util.UserUtil;
import org.springframework.stereotype.Service;

/**
 * Service class for user-related operations.
 */
@Service
public class UserService {

  private final UserRepository userRepository;
  private final UserUtil userUtil;

  /**
   * Constructor for UserService.
   *
   * @param userRepository the UserRepository
   * @param userUtil the UserUtil
   */
  public UserService(UserRepository userRepository, UserUtil userUtil) {
    this.userRepository = userRepository;
    this.userUtil = userUtil;
  }

  /**
   * Sets the active status of the current user.
   *
   * @param active the active status to set
   */
  public void setActive(boolean active) {
    userRepository.findAndSetActiveById(userUtil.getCurUserId(), active);
  }
}
