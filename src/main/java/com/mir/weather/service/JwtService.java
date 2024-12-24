package com.mir.weather.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mir.weather.config.dto.JwtProperties;
import com.mir.weather.dto.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;

/**
 * Service class for managing JWT tokens.
 */
@Service
public class JwtService {

  private final JwtProperties properties;

  /**
   * Constructor for JwtService.
   *
   * @param properties the JWT properties
   */
  public JwtService(JwtProperties properties) {
    this.properties = properties;
  }

  /**
   * Extracts the username from the JWT token.
   *
   * @param token the JWT token
   * @return the username
   */
  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  /**
   * Extracts a specific claim from the JWT token.
   *
   * @param <T> the type of the claim
   * @param token the JWT token
   * @param claimsResolver the function to resolve the claim
   * @return the extracted claim
   */
  private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  /**
   * Generates a JWT token for a user.
   *
   * @param user the user
   * @return the generated JWT token
   * @throws JsonProcessingException if there is an error processing JSON
   */
  public String generateToken(User user) throws JsonProcessingException {
    return generateToken(new HashMap<>(), user);
  }

  /**
   * Generates a JWT token with additional claims for a user.
   *
   * @param extraClaims additional claims to include in the token
   * @param user the user
   * @return the generated JWT token
   * @throws JsonProcessingException if there is an error processing JSON
   */
  public String generateToken(Map<String, Object> extraClaims, User user) {
    return buildToken(extraClaims, user, properties.expirationTime());
  }

  /**
   * Retrieves the expiration time for JWT tokens.
   *
   * @return the expiration time in milliseconds
   */
  public long getExpirationTime() {
    return properties.expirationTime();
  }

  /**
   * Builds a JWT token with the specified claims and expiration time.
   *
   * @param extraClaims additional claims to include in the token
   * @param user the user
   * @param expiration the expiration time in milliseconds
   * @return the generated JWT token
   */
  private String buildToken(Map<String, Object> extraClaims, User user,
      long expiration) {

    long currentTime = System.currentTimeMillis();

    return Jwts
        .builder().claims(extraClaims).subject(user.getUsername())
        .issuedAt(new Date(currentTime))
        .expiration(new Date(currentTime + expiration))
        .signWith(getSignInKey())
        .compact();
  }

  /**
   * Checks if a JWT token is expired.
   *
   * @param token the JWT token
   * @return true if the token is expired, false otherwise
   */
  public boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  /**
   * Extracts the expiration date from the JWT token.
   *
   * @param token the JWT token
   * @return the expiration date
   */
  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  /**
   * Extracts all claims from the JWT token.
   *
   * @param token the JWT token
   * @return the claims
   */
  private Claims extractAllClaims(String token) {
    return Jwts.parser().verifyWith(getSignInKey()).build()
        .parseSignedClaims(token).getPayload();
  }

  /**
   * Retrieves the signing key for the JWT token.
   *
   * @return the signing key
   */
  private SecretKey getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(properties.secretKey());
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
