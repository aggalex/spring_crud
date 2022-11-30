package com.backend.dove.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Configuration
public class TemplateConfiguration {

    @Bean
    public SpringTemplateEngine templateEngine() {
        return new SpringTemplateEngine();
    }

}
