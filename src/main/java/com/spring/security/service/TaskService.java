package com.spring.security.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.spring.security.entity.Task;
import com.spring.security.entity.DTO.TaskDTO;
import com.spring.security.entity.Response.TaskResponse;

public interface TaskService {

    public List<Task> findAllTaskByUser();

    public TaskResponse findTaskById(Integer id);
    
    public TaskResponse createTask(TaskDTO task, MultipartFile file);

    public TaskResponse updateTask(Integer id, TaskDTO task, MultipartFile file);

    public void deleteTask(Integer id);


}
