package com.spring.security.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.spring.security.entity.FileData;
import com.spring.security.repository.FileRepository;
import com.spring.security.service.StorageService;

@Service
public class StorageServiceImpl implements StorageService {

    @Autowired
    private FileRepository fileRepository;

    private final Path root = Paths.get("src/main/resources/static/images");

    @Override
    public String uploadFile(String path, MultipartFile file) throws IOException {
        if(!Files.exists(root)){
            Files.createDirectories(root);
        }

        FileData fileData = fileRepository.save(
            FileData.builder().name(file.getOriginalFilename()).type(file.getContentType()).filePath(path).build()
        );
       
        Files.copy(file.getInputStream(), this.root.resolve(fileData.getName()));
      

        if (fileData != null) {
            return "file uploaded successfully : " + path;
        }
        return null;
        
    }
    
}
