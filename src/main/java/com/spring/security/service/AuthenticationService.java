package com.spring.security.service;

import com.spring.security.entity.AuthenticationResponse;
import com.spring.security.entity.User;

public interface AuthenticationService {
    
    public AuthenticationResponse register(User request);

    public AuthenticationResponse authenticate(User request);
}
