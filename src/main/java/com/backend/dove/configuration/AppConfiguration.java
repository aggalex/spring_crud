package com.backend.dove.configuration;

import com.backend.dove.util.PasswordGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class AppConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PasswordGenerator passwordGenerator() {
        return new PasswordGenerator (
                PasswordGenerator.Characters.lowercase,
                PasswordGenerator.Characters.uppercase,
                PasswordGenerator.Characters.numbers
        )
                .setLength(128);
    }
}
