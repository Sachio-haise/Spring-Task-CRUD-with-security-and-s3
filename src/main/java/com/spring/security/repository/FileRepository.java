package com.spring.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.security.entity.FileData;

public interface FileRepository extends JpaRepository<FileData, Integer> {
    
}
