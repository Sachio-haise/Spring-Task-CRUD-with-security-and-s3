package com.spring.security.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spring.security.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    
    List<Task> findAllByUserId(Integer userId);

    @Query("SELECT t FROM Task t WHERE t.id = :id AND t.user.id = :userId")
    Optional<Task> findTaskByUserIdAndId(@Param("id") Integer id, @Param("userId") Integer userId);
}
