package com.spring.security.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.spring.security.entity.User;

public class UserSerializer extends JsonSerializer<User> {

    @Override
    public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
       jsonGenerator.writeStartObject();
       jsonGenerator.writeNumberField("id", user.getId());
       jsonGenerator.writeStringField("first_name", user.getFirstName());
       jsonGenerator.writeStringField("last_name",  user.getLastName());
       jsonGenerator.writeStringField("email",user.getUsername());
       jsonGenerator.writeStringField("role", user.getRole().toString());
       jsonGenerator.writeStringField("filePath", user.getFilePath());
       jsonGenerator.writeObjectField("task", user.getTasks());
       jsonGenerator.writeEndObject();
    }
    
}
