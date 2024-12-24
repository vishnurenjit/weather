package com.mir.weather.controller;

import com.mir.weather.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user")
@RestController
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PatchMapping("/active/{active}")
  public ResponseEntity activate(@PathVariable boolean active) {
    userService.setActive(active);
    return ResponseEntity.ok().build();
  }
}
