package com.mir.weather.dto;

import com.mir.weather.entity.UserDoc;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDto implements UserDetails, User {

  private final UserDoc userDoc;

  public UserDto(UserDoc userDoc) {
    this.userDoc = userDoc;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  @Override
  public String getPassword() {
    return userDoc.getPassword();
  }

  @Override
  public String getUsername() {
    return userDoc.getUsername();
  }

  @Override
  public UUID getId() {
    return userDoc.getId();
  }

  @Override
  public boolean isActive() {
    return userDoc.isActive();
  }

  @Override
  public boolean isAccountNonExpired() {
    return UserDetails.super.isAccountNonExpired();
  }

  @Override
  public boolean isAccountNonLocked() {
    return UserDetails.super.isAccountNonLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return UserDetails.super.isCredentialsNonExpired();
  }

  @Override
  public boolean isEnabled() {
    return UserDetails.super.isEnabled();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof UserDto userDto)) {
      return false;
    }
    return Objects.equals(userDoc, userDto.userDoc);
  }
}
