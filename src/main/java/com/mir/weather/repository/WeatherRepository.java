package com.mir.weather.repository;

import com.mir.weather.entity.WeatherDoc;
import java.util.List;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository interface for managing WeatherEntity objects in the MongoDB database.
 */
public interface WeatherRepository extends MongoRepository<WeatherDoc, UUID> {

  /**
   * Finds a list of WeatherEntity objects by the user ID.
   *
   * @param curUserId the UUID of the user
   * @return a list of WeatherEntity objects
   */
  List<WeatherDoc> findByUserId(UUID curUserId);

  /**
   * Deletes WeatherEntity objects by the user ID.
   *
   * @param curUserId the UUID of the user
   * @return the number of entities deleted
   */
  long deleteByUserId(UUID curUserId);
}
