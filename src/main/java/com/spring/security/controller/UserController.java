package com.spring.security.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spring.security.entity.User;
import com.spring.security.entity.DTO.PasswordChangeDTO;
import com.spring.security.entity.Response.UserResponse;
import com.spring.security.service.AuthenticationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    
    private final AuthenticationService authenticationService;


    @GetMapping    
    public ResponseEntity<UserResponse> getUser() {
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.getUser());
    }

    @PostMapping("/update")
    public ResponseEntity<User> updateUser(@ModelAttribute User request,MultipartFile file) throws IOException {
         return ResponseEntity.ok(authenticationService.updateProfile(request, file));
    }   

    @PostMapping("/change-password")
    public ResponseEntity<User> changePassword(@RequestBody PasswordChangeDTO request) {
     return ResponseEntity.status(HttpStatus.OK).body(authenticationService.changePassword(request));   
    }
}
