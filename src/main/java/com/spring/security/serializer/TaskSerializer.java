package com.spring.security.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.spring.security.entity.Task;

public class TaskSerializer extends JsonSerializer<Task>{

    @Override
    public void serialize(Task task, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", task.getId());
        jsonGenerator.writeStringField("name", task.getName());
        jsonGenerator.writeStringField("description", task.getDescription());
        jsonGenerator.writeNumberField("priority", task.getPriority());
        jsonGenerator.writeStringField("status", task.getStatus().toString());
       // jsonGenerator.writeObjectField("user", task.getUser());
        jsonGenerator.writeStringField("filePath", task.getFilePath());
        jsonGenerator.writeStringField("created_at", task.getCreated_at().toString());
        jsonGenerator.writeStringField("updated_at", task.getUpdated_at().toString());
        jsonGenerator.writeEndObject();
    }
    
}
