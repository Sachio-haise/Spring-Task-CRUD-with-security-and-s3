package com.spring.security.service;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.security.entity.AuthenticationResponse;
import com.spring.security.entity.Token;
import com.spring.security.entity.User;
import com.spring.security.repository.TokenRepository;
import com.spring.security.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

  private final JwtService jwtService;

  private final PasswordEncoder passwordEncoder;

  private final UserRepository userRepository;

  private final AuthenticationManager authenticationManager;

  private final TokenRepository tokenRepository;

  @Override
  public AuthenticationResponse register(User request) {
    User user = User.builder()
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .username(request.getUsername())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(request.getRole())
        .build();
    userRepository.save(user);
    String jwt = jwtService.generateToken(user);

    // save the generated token
    saveUserToken(user, jwt);

    return AuthenticationResponse.builder().token(jwt).build();

  }

  private void saveUserToken(User user, String jwt) {
    Token token = new Token();
    token.setToken(jwt);
    token.setUser(user);
    tokenRepository.save(token);

  }

  @Override
  public AuthenticationResponse authenticate(User request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getUsername(), request.getPassword()));

    User user = userRepository.findByUsername(request.getUsername()).orElseThrow();

    String jwt = jwtService.generateToken(user);

    revokeAllTokenByUser(user);
    saveUserToken(user, jwt);

    return AuthenticationResponse.builder().token(jwt).build();
  }

  private void revokeAllTokenByUser(User user) {
    List<Token> validTokenListByUser = tokenRepository.findAllTokenByUser(user.getId());

    if (!validTokenListByUser.isEmpty()) {
      validTokenListByUser.forEach(
          t -> {
            t.setLoggedOut(true);
          });
    }
    tokenRepository.saveAll(validTokenListByUser);
  }
}
