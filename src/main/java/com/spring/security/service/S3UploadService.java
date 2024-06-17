package com.spring.security.service;

import org.springframework.web.multipart.MultipartFile;

public interface S3UploadService {
    public String uploadImage(final MultipartFile file, final String folderName);

    public String deleteImage(final String fileName);
}
