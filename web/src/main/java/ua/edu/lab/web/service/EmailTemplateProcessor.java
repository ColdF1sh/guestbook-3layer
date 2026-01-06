package ua.edu.lab.web.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.util.Map;

@Component
public class EmailTemplateProcessor {

    private static final Logger logger = LoggerFactory.getLogger(EmailTemplateProcessor.class);

    private final Configuration freemarkerConfig;

    public EmailTemplateProcessor(@Qualifier("freemarkerEmailConfig") Configuration freemarkerEmailConfig) {
        this.freemarkerConfig = freemarkerEmailConfig;
    }

    public String renderTemplate(String templateName, Map<String, Object> model) {
        try {
            Template template = freemarkerConfig.getTemplate(templateName);
            StringWriter writer = new StringWriter();
            template.process(model, writer);
            return writer.toString();
        } catch (Exception e) {
            logger.error("Failed to render email template: " + templateName, e);
            logger.error("Template model data: " + model);
            throw new RuntimeException("Failed to render email template: " + templateName, e);
        }
    }
}

