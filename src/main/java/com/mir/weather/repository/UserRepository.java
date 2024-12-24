package com.mir.weather.repository;

import com.mir.weather.entity.UserDoc;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Update;

/**
 * Repository interface for managing UserEntity objects in the MongoDB database.
 */
public interface UserRepository extends MongoRepository<UserDoc, UUID> {

  /**
   * Finds a UserEntity by the username.
   *
   * @param username the username
   * @return an Optional containing the UserEntity if found, otherwise empty
   */
  Optional<UserDoc> findByUsername(String username);

  /**
   * Updates the active status of a user by user ID.
   *
   * @param id the UUID of the user
   * @param active the active status to set
   */
  @Update("{ '$set' : { 'active' : ?1 } }")
  void findAndSetActiveById(UUID id, boolean active);
}
