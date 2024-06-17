package com.spring.security.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.spring.security.entity.User;
import com.spring.security.repository.TokenRepository;
import com.spring.security.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService{

    private static final String secret_key = "e4173bcf4e8c54020fa188f4f483751ab3e052f7659965fd0ee93d518d06cdeb";

    private final TokenRepository tokenRepository;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Override
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public Integer extractUserId(String token){
        return extractClaim(token, claims -> claims.get("userId", Integer.class));
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimResolver){
        Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    @Override
     public boolean isValid(String token, UserDetails user){
        final String username = extractUsername(token);

        boolean validToken = tokenRepository
                .findByToken(token)
                .map(t -> !t.isLoggedOut())
                .orElse(false);

        return (username.equals(user.getUsername())) && !isTokenExpired(token) && validToken;
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
    }

    @Override
    public String generateToken(User user) {

      Map<String,Object> claims = new HashMap<>();
      claims.put("userId", user.getId());
      String token = Jwts.builder().setClaims(claims).setSubject(user.getUsername()).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)).signWith(getSignKey()).compact();
      return token;
    }
    

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(secret_key);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String extractTokenFromRequest() {
         String authHeader = httpServletRequest.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        String token = authHeader.substring(7);
        return token;
    }
}
