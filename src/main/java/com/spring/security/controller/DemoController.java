package com.spring.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Hello from secured endpoint!");
    }

    @GetMapping("/test-admin")
    public ResponseEntity<String> testAdmin() {
        return ResponseEntity.ok("Hello from secured endpoint for ADMIN!");
    }
}
