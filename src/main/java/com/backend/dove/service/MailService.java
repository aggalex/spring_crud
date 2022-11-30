package com.backend.dove.service;

import com.backend.dove.dto.MailDto;
import org.flywaydb.core.internal.resource.filesystem.FileSystemResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class MailService {

    @Value("{spring.mail.username}")
    private String sender;

    JavaMailSender javaMailSender;

    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * Sends an email without an attachment
     *
     * @param details the data transfer object that describes the details of the email
     */
    public void sendSimpleMail(MailDto details) {
        // Creating a simple mail message
        SimpleMailMessage mailMessage
            = new SimpleMailMessage();

        // Setting up necessary details
        mailMessage.setFrom(sender);
        mailMessage.setTo(details.getRecipient());
        mailMessage.setText(details.getBody());
        mailMessage.setSubject(details.getSubject());

        // Sending the mail
        javaMailSender.send(mailMessage);

    }

}
