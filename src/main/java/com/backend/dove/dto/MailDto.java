package com.backend.dove.dto;

import org.springframework.beans.factory.annotation.Value;

public class MailDto {

    private String recipient;
    private String body;
    private String subject;

    public String getRecipient() {
        return recipient;
    }

    public MailDto setRecipient(String recipient) {
        this.recipient = recipient;
        return this;
    }

    public String getBody() {
        return body;
    }

    public MailDto setBody(String body) {
        this.body = body;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public MailDto setSubject(String subject) {
        this.subject = subject;
        return this;
    }

}
