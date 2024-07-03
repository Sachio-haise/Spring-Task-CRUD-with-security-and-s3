package com.spring.security.service;

import org.thymeleaf.context.Context;

public interface EmailService {
    
    public boolean sendEmail(String to, String subject,String templateName, Context context);
}
