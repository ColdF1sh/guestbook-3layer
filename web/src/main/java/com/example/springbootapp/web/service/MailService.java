package com.example.springbootapp.web.service;

import com.example.springbootapp.core.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class MailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);

    private final JavaMailSender mailSender;
    private final String adminRecipient;
    private final String baseUrl;
    private final String mailUsername;
    private final String mailPassword;

    public MailService(JavaMailSender mailSender,
                       @Value("${app.mail.admin:}") String adminRecipient,
                       @Value("${app.base-url:http://localhost:8080}") String baseUrl,
                       @Value("${spring.mail.username:}") String mailUsername,
                       @Value("${spring.mail.password:}") String mailPassword) {
        this.mailSender = mailSender;
        this.adminRecipient = adminRecipient;
        this.baseUrl = baseUrl;
        this.mailUsername = mailUsername;
        this.mailPassword = mailPassword;
    }

    public void sendNewBookNotification(Book book) {
        if (!StringUtils.hasText(adminRecipient)
            || !StringUtils.hasText(mailUsername)
            || !StringUtils.hasText(mailPassword)) {
            log.warn("Mail credentials not set; skipping email");
            return;
        }

        Set<String> recipients = new LinkedHashSet<>();
        recipients.add(adminRecipient);
        if (!adminRecipient.equalsIgnoreCase(mailUsername)) {
            recipients.add(mailUsername);
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipients.toArray(new String[0]));
        message.setSubject("New book added: " + book.getTitle());
        message.setText("A new book was added.\n" +
            "Title: " + book.getTitle() + "\n" +
            "Author: " + book.getAuthor() + "\n" +
            "Year: " + book.getYear() + "\n" +
            "View: " + baseUrl + "/books/" + book.getId());

        try {
            mailSender.send(message);
        } catch (MailException ex) {
            log.warn("Failed to send mail notification: {}", ex.getMessage());
        }
    }
}
