package com.spring.security.service;

import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;

import com.spring.security.entity.User;

import io.jsonwebtoken.Claims;

public interface JwtService {
  String generateToken(User user);   

  boolean isValid(String token, UserDetails user);

  String extractUsername(String token);

  <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
}
