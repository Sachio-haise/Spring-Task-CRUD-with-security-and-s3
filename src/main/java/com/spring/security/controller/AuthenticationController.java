package com.spring.security.controller;

import org.springframework.web.bind.annotation.RestController;

import com.spring.security.entity.User;
import com.spring.security.entity.Response.AuthenticationResponse;
import com.spring.security.entity.Response.UserResponse;
import com.spring.security.service.AuthenticationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserDetailsService userDetailsService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid User request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody User request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/forget-password")
    public ResponseEntity<Boolean> forgotPassword(@ModelAttribute User request) {
        return ResponseEntity.ok(authenticationService.forgotPassword(request));
    }

    @PostMapping("/check-code")
    public ResponseEntity<User> checkCode(@ModelAttribute User request) {
        return ResponseEntity.ok(authenticationService.checkCode(request));
    }
    
    @PostMapping("/update-password")
    public ResponseEntity<User> updatePassword(@ModelAttribute User request) {
        return ResponseEntity.ok(authenticationService.updatePassword(request));
    }

    @PostMapping("/user")
    public ResponseEntity<UserResponse> getUser(@RequestBody User request) {
        return ResponseEntity.ok(authenticationService.getUser(request));
    }
    

}
