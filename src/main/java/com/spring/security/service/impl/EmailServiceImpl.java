package com.spring.security.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.spring.security.service.EmailService;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService{

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public boolean sendEmail(String to, String subject, String templateName, Context context) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

        try{
          String htmlContext = templateEngine.process(templateName, context);
          helper.setTo(to);
          helper.setSubject(subject);
          helper.setText(htmlContext, true);
          mailSender.send(mimeMessage);
          return true;
        }catch(Exception e){
            System.out.println("Error Message: " + e.getMessage());
            return false;
        }
   
    }
    
}
