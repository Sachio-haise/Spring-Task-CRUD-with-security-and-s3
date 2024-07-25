package com.spring.security.entity.DTO;

import com.spring.security.entity.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDTO {
    private Integer id;

    private String name;

    private String description;

    private String priority;

    private Status status;

    private String filePath;

    private String dueDate;

    private String dueTime;

    private String userId;

}
