package com.mir.weather.entity;

import com.mir.weather.dto.User;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
/**
 * Document to store user info
 */
@Getter
@Setter
public class UserDoc implements User {

  @Id
  private UUID id;

  private String firstName;

  private String lastName;

  @Indexed(unique = true)
  private String username;

  private String password;

  private boolean active;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof UserDoc that)) {
      return false;
    }
    return isActive() == that.isActive() && Objects.equals(getId(),
        that.getId()) && Objects.equals(getFirstName(),
        that.getFirstName()) && Objects.equals(getLastName(),
        that.getLastName()) && Objects.equals(getUsername(),
        that.getUsername()) && Objects.equals(getPassword(),
        that.getPassword());
  }
}
