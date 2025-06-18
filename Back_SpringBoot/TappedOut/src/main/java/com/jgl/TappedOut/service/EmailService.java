package com.jgl.TappedOut.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;

    public void sendEmail(String to, String subject, String htmlContent) {
        if (to == null || to.isBlank()) {
            log.error("Intento de enviar email sin destinatario");
            throw new IllegalArgumentException("El destinatario no puede estar vacío");
        }

        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
            
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            
            mailSender.send(msg);
            log.info("Email enviado correctamente a {}", to);
        } catch (MailException e) {
            log.error("Error de MailException\nError msg: {}", e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        } catch (MessagingException e) {
            log.error("Error de MessagingException\nError msg: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error sending email\nError msg: " + e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void sendWelcomeEmail(String to, String username) {
        String subject = "Welcome to TappedOut!";

        Context context = new Context();
        context.setVariable("username", username);

        String htmlContent = templateEngine.process("emails/welcome", context);

        sendEmail(to, subject, htmlContent);
    }

    public void sendEventInscriptionEmail(String to, String eventName, String eventDate) {
        String subject = "You have been inscribed to an event!";
        
        Context context = new Context();
        context.setVariable("eventName", eventName);
        context.setVariable("eventDate", eventDate);

        String htmlContent = templateEngine.process("emails/event-inscription", context);

        sendEmail(to, subject, htmlContent);
    }

    public void sendEventCreationEmail(String to, String eventName, String eventDate) {
        String subject = "Your event has been created!";
        
        Context context = new Context();
        context.setVariable("eventName", eventName);
        context.setVariable("eventDate", eventDate);

        String htmlContent = templateEngine.process("emails/event-creation", context);

        sendEmail(to, subject, htmlContent);
    }

    public void sendEventUpdateNotification(List<String> recipients, String eventName, String eventDate) {
        String subject = "¡Un evento en TappedOut ha sido actualizado!";
        
        Context context = new Context();
        context.setVariable("eventName", eventName);
        context.setVariable("eventDate", eventDate);
        
        String htmlContent = templateEngine.process("emails/event-update", context);
        
        recipients.forEach(to -> sendEmail(to, subject, htmlContent));
    }

    public void sendRememberNotification(List<String> recipients, String eventName, LocalDate date) {
        String subject = "Remember: " + eventName + " is about to start!";
        String formattedDate = date.format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy"));
        
        Context context = new Context();
        context.setVariable("eventName", eventName);
        context.setVariable("eventDate", formattedDate);
        
        String htmlContent = templateEngine.process("emails/event-reminder", context);
        
        recipients.forEach(to -> sendEmail(to, subject, htmlContent));
    }
}