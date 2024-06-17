package com.spring.security.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    
    public String uploadFile(String path, MultipartFile file) throws IOException;
}
