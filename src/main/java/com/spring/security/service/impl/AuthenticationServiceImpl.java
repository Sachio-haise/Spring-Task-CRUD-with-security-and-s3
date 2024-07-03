package com.spring.security.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;

import com.spring.security.entity.Token;
import com.spring.security.entity.User;
import com.spring.security.entity.Response.AuthenticationResponse;
import com.spring.security.entity.Response.UserResponse;
import com.spring.security.exception.ValidationException;
import com.spring.security.repository.TokenRepository;
import com.spring.security.repository.UserRepository;
import com.spring.security.service.AuthenticationService;
import com.spring.security.service.EmailService;
import com.spring.security.service.JwtService;
import com.spring.security.service.S3UploadService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

  private final JwtService jwtService;

  private final PasswordEncoder passwordEncoder;

  private final UserRepository userRepository;

  private final AuthenticationManager authenticationManager;

  private final TokenRepository tokenRepository;

  private final S3UploadService s3UploadService;

  private final EmailService emailService;

  private final static Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

  
  @Override
  public AuthenticationResponse register(User request) {

    Map<String,String> errors = new HashMap<>();

    if (userRepository.existsByUsername(request.getUsername())) {
      errors.put("username", "Username Already Exist.");
    }

    if(!errors.isEmpty()) {
      throw new ValidationException(errors);
    }

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

    Map<String,String> errors = new HashMap<>();

    if(request.getUsername() == null || request.getUsername().trim().isEmpty()){
      errors.put("email", "Email field is required.");
    }

    if(request.getPassword() == null || request.getPassword().trim().isEmpty()){
      errors.put("password", "Password field is required.");
    }

    if(!errors.isEmpty()){
      throw new ValidationException(errors);
    }

    User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> {
      throw new ValidationException(Map.of("email", "Email doesn\'t exist."));
    });

    if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) throw new ValidationException(Map.of("password", "Invalid Password."));

    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getUsername(), request.getPassword()));

    String jwt = jwtService.generateToken(user);

    revokeAllTokenByUser(user);
    saveUserToken(user, jwt);

    return AuthenticationResponse.builder().token(jwt).build();
  }

  @Override
  public UserResponse getUser(User request){
    User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> {
      throw new ValidationException(Map.of("email","User not found."));
    });

     UserResponse userResponse = new UserResponse(user);
    return userResponse;
  }

  @Override
  public boolean forgotPassword(User request) {
    User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> {
      throw new ValidationException(Map.of("email","User not found."));
    });
    Random random = new Random();
    int otp = 100000 + random.nextInt(900000);
    Context context = new Context();
    context.setVariable("message", otp);
    context.setVariable("name", user.getFirstName() + " " + user.getLastName());
    context.setVariable("subject", "Forgot Password");
    emailService.sendEmail(user.getUsername(), "Forgot Password", "emailTemplate", context);
    user.setCode(String.valueOf(otp));
    userRepository.save(user);
    return true;
  }

  @Override
  public User checkCode(User request) {
    User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> {
      throw new ValidationException(Map.of("email","User not found."));
    });
    if(user.getCode() == null || !user.getCode().equals(request.getCode())) {
      throw new ValidationException(Map.of("code","Invalid code."));
    }
    return user;
  }

  @Override
  public User updatePassword(User request) {
    User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> {
      throw new ValidationException(Map.of("email", "User not found."));
    });
    if(user.getCode() == null || !user.getCode().equals(request.getCode())) {
      throw new ValidationException(Map.of("code","Invalid code."));
    }
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setCode(null);
    return userRepository.save(user);
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

  @Override
  public User updateProfile(User request, MultipartFile file) throws IOException {

    String token = jwtService.extractTokenFromRequest();
    Integer userId = jwtService.extractUserId(token);
    User user = userRepository.findById(userId).orElseThrow(() -> {
      throw new RuntimeException("User not found");
    });

    if (file != null) {
      if(user.getFilePath() != null && !user.getFilePath().isEmpty()) {
        s3UploadService.deleteImage(user.getFilePath());
      }
      String filePath = s3UploadService.uploadImage(file, "user");
      user.setFilePath(filePath);
    }

    user.setFirstName(request.getFirstName());
    user.setLastName(request.getLastName());
    user.setRole(request.getRole());
   
    return userRepository.save(user);
  }
}
