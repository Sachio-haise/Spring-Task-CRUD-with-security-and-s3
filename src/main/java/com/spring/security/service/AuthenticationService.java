package com.spring.security.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.spring.security.entity.User;
import com.spring.security.entity.Response.AuthenticationResponse;
import com.spring.security.entity.Response.UserResponse;

public interface AuthenticationService {
    
    public AuthenticationResponse register(User request);

    public AuthenticationResponse authenticate(User request);

    public UserResponse getUser(User request);

    public User updateProfile(User request, MultipartFile file) throws IOException;

    public boolean forgotPassword(User request);

    public User checkCode(User user);

    public User updatePassword(User user);
}
