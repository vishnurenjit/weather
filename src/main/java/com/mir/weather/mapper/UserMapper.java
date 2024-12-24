package com.mir.weather.mapper;

import com.mir.weather.dto.RegisterUser;
import com.mir.weather.entity.UserDoc;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting RegisterUser DTO to UserEntity.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

  /**
   * Maps a RegisterUser DTO to a UserEntity.
   *
   * @param registerUser the RegisterUser DTO to map
   * @return the mapped UserEntity
   */
  UserDoc mapToEntity(RegisterUser registerUser);
}
