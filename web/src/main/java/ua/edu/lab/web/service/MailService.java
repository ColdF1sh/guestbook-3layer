package ua.edu.lab.web.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ua.edu.lab.core.domain.Book;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class MailService {

    private final JavaMailSender mailSender;
    private final EmailTemplateProcessor templateProcessor;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.mail.to:${spring.mail.username}}")
    private String toEmail;

    public MailService(JavaMailSender mailSender, EmailTemplateProcessor templateProcessor) {
        this.mailSender = mailSender;
        this.templateProcessor = templateProcessor;
    }

    public void sendNewBookEmail(Book book) {
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("title", book.getTitle());
            model.put("author", book.getAuthor());
            int currentYear = LocalDateTime.now().getYear();
            model.put("year", Integer.valueOf(currentYear));
            model.put("added", LocalDateTime.now().toString());

            String htmlContent = templateProcessor.renderTemplate("new_book.ftl", model);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("New Book Added: " + book.getTitle());
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email for book: " + book.getTitle(), e);
        }
    }
}

