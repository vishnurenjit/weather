package com.mir.weather.filter;

import com.mir.weather.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * Filter for handling JWT authentication in incoming HTTP requests.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final HandlerExceptionResolver handlerExceptionResolver;
  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;

  /**
   * Constructor for JwtAuthenticationFilter.
   *
   * @param jwtService the JWT service for token operations
   * @param userDetailsService the user details service for loading user information
   * @param handlerExceptionResolver the handler for resolving exceptions
   */
  public JwtAuthenticationFilter(
      JwtService jwtService,
      UserDetailsService userDetailsService,
      HandlerExceptionResolver handlerExceptionResolver
  ) {
    this.jwtService = jwtService;
    this.userDetailsService = userDetailsService;
    this.handlerExceptionResolver = handlerExceptionResolver;
  }

  /**
   * Filters incoming requests to perform JWT authentication.
   *
   * @param request the HTTP request
   * @param response the HTTP response
   * @param filterChain the filter chain
   * @throws ServletException if a servlet exception occurs
   * @throws IOException if an I/O exception occurs
   */
  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    final String authHeader = request.getHeader("Authorization");

    // If the Authorization header is missing or does not start with "Bearer ", continue the filter chain
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      // Extract the JWT token from the Authorization header
      final String jwt = authHeader.substring(7);
      final String username = jwtService.extractUsername(jwt);

      // Get the current authentication from the security context
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      // If the username is valid and the user is not authenticated, load user details and set authentication
      if (username != null && authentication == null) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

        // If the user details are valid and the token is not expired, set the authentication
        if (userDetails != null && !jwtService.isTokenExpired(jwt)) {
          UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
              userDetails,
              null,
              userDetails.getAuthorities()
          );

          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authToken);
        }
      }

      // Continue the filter chain
      filterChain.doFilter(request, response);
    } catch (Exception exception) {
      // Handle any exceptions that occur during the filter process
      handlerExceptionResolver.resolveException(request, response, null, exception);
    }
  }
}
