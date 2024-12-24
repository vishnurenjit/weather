package com.mir.weather.service;

import com.mir.weather.dto.LoginUser;
import com.mir.weather.dto.RegisterUser;
import com.mir.weather.dto.User;
import com.mir.weather.dto.UserDto;
import com.mir.weather.entity.UserDoc;
import com.mir.weather.mapper.UserMapper;
import com.mir.weather.repository.UserRepository;
import java.util.UUID;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * Service class for managing user authentication and registration.
 */
@Service
public class AuthenticationService {

  private final UserRepository userRepository;
  private final AuthenticationManager authenticationManager;
  private final UserMapper userMapper;

  /**
   * Constructor for AuthenticationService.
   *
   * @param userRepository the UserRepository
   * @param authenticationManager the AuthenticationManager
   * @param userMapper the UserMapper
   */
  public AuthenticationService(
      UserRepository userRepository,
      AuthenticationManager authenticationManager,
      UserMapper userMapper) {
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    this.userMapper = userMapper;
  }

  /**
   * Registers a new user.
   *
   * @param userInfo the user information for registration
   */
  public void signup(RegisterUser userInfo) {
    UserDoc userDoc = userMapper.mapToEntity(userInfo);
    userDoc.setId(UUID.randomUUID());
    userDoc.setActive(true);
    userRepository.save(userDoc);
  }

  /**
   * Authenticates a user based on the provided login information.
   *
   * @param input the login information
   * @return the authenticated user details
   */
  public User authenticate(LoginUser input) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            input.getUserName(),
            input.getPassword()
        )
    );

    return new UserDto(userRepository.findByUsername(input.getUserName())
        .orElseThrow());
  }
}
