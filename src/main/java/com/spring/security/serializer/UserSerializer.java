package com.spring.security.serializer;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.spring.security.entity.User;

public class UserSerializer extends JsonSerializer<User> {

    @Value("${cloud.aws.s3.endpoint}")
    private String endPoint;

    @Override
    public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
       jsonGenerator.writeStartObject();
       jsonGenerator.writeNumberField("id", user.getId());
       jsonGenerator.writeStringField("firstName", user.getFirstName());
       jsonGenerator.writeStringField("lastName",  user.getLastName());
       jsonGenerator.writeStringField("description", user.getDescription());
       jsonGenerator.writeStringField("email",user.getUsername());
       jsonGenerator.writeStringField("role", user.getRole().toString());
       jsonGenerator.writeStringField("filePath",user.getFilePath() == null ? null : endPoint + "/"
               + user.getFilePath());
       jsonGenerator.writeObjectField("task", user.getTasks());
       jsonGenerator.writeEndObject();
    }
    
}
