package com.spring.security.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spring.security.entity.Task;
import com.spring.security.entity.DTO.TaskDTO;
import com.spring.security.entity.Response.TaskResponse;
import com.spring.security.service.TaskService;

import java.io.IOException;
import java.util.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
public class TaskController {

    @Autowired
    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<Task>> getTaskByUser() {
        return ResponseEntity.ok(taskService.findAllTaskByUser());
    }
    

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Integer id) {
        return ResponseEntity.ok(taskService.findTaskById(id));
    }
    
    
    @PostMapping("/create")
    public ResponseEntity<TaskResponse> createTask(@Valid @ModelAttribute TaskDTO taskDTO,@RequestParam(value = "file", required = false) MultipartFile file) throws IOException{
        TaskResponse entity = taskService.createTask(taskDTO, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(entity);
    }
    

    @PostMapping("/update/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Integer id, @Valid @ModelAttribute TaskDTO taskDTO, @RequestParam(value = "file",required = false) MultipartFile file) throws IOException {
        TaskResponse entity = taskService.updateTask(id,taskDTO,file);
        return ResponseEntity.status(HttpStatus.OK).body(entity);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Integer id) {
        taskService.deleteTask(id);
        return  ResponseEntity.status(HttpStatus.OK).body("Task deleted successfully");
    }
    
}
