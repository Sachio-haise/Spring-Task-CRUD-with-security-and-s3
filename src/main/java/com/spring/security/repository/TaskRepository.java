package com.spring.security.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.security.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    
    List<Task> findAllByUserId(Integer userId);
}
