package com.spring.security.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.spring.security.entity.Task;
import com.spring.security.entity.User;
import com.spring.security.entity.DTO.TaskDTO;
import com.spring.security.entity.Response.TaskResponse;
import com.spring.security.exception.ValidationException;
import com.spring.security.repository.TaskRepository;
import com.spring.security.repository.UserRepository;
import com.spring.security.service.JwtService;
import com.spring.security.service.S3UploadService;
import com.spring.security.service.TaskService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    @Autowired
    private final TaskRepository taskRepository;


    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final S3UploadService s3UploadService;

    @Autowired
    private final JwtService jwtService;

    @Override
    public List<Task> findAllTaskByUser() {
        String token = jwtService.extractTokenFromRequest();
        Integer userId = jwtService.extractUserId(token);
        System.out.println("the id is -...........> " + userId);
        return taskRepository.findAllByUserId(userId);
    }

    @Override
    public TaskResponse findTaskById(Integer id) {
       return new TaskResponse(taskRepository.findById(id).orElseThrow( () -> new RuntimeException("Task not found")));
    }

    @Override
    public TaskResponse createTask(TaskDTO request, MultipartFile file) {
        String filePath = "";
        if (file != null) {
            filePath = s3UploadService.uploadImage(file, "task");
        }

        Task task = new Task();
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> {
            throw new ValidationException(Map.of("email", "Email doesn\'t exist."));
        });
        task.setUser(user);
        task.setName(request.getName());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        task.setStatus(request.getStatus());
        task.setFilePath(filePath);

        return new TaskResponse(taskRepository.save(task));
    }

    @Override
    public TaskResponse updateTask(Integer id, TaskDTO taskDTO, MultipartFile file) {
        User user = userRepository.findById(taskDTO.getUserId()).orElseThrow(() -> {
            throw new ValidationException(Map.of("email", "Email doesn\'t exist."));
        });

        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        if(file != null){
            if(task.getFilePath() != null && !task.getFilePath().isEmpty()) {
                s3UploadService.deleteImage(task.getFilePath());
            }
            String filePath = s3UploadService.uploadImage(file, "task");
            task.setFilePath(filePath);
        }
        task.setId(id);
        task.setUser(user);
        task.setName(taskDTO.getName());
        task.setDescription(taskDTO.getDescription());
        task.setPriority(taskDTO.getPriority());
        task.setStatus(taskDTO.getStatus());
        return new TaskResponse(taskRepository.save(task));
    }

    @Override
    public void deleteTask(Integer id) {
        if(taskRepository.existsById(id)) {
            if(taskRepository.findById(id).get().getFilePath() != null && !taskRepository.findById(id).get().getFilePath().isEmpty()) { 
                s3UploadService.deleteImage(taskRepository.findById(id).get().getFilePath());
            }
            taskRepository.deleteById(id);
        }
    }
    
}
