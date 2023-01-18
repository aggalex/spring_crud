package com.backend.dove.service;


import com.backend.dove.dto.*;
import com.backend.dove.entity.User;
import com.backend.dove.repository.UserRepository;
import com.backend.dove.util.PasswordGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

@Service
public class UserService {

    private UserRepository repository;

    private PasswordEncoder passwordEncoder;

    private MailService mailService;

    private PasswordGenerator generator;

    private SpringTemplateEngine templateEngine;

    public UserService(UserRepository repository,
                       PasswordEncoder passwordEncoder,
                       MailService mailService,
                       PasswordGenerator generator,
                       SpringTemplateEngine templateEngine) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.generator = generator;
        this.templateEngine = templateEngine;
    }

    public UserInfoDto login(UserLoginDto loginDto) throws Exception {
        var user = repository.getUserByEmail(loginDto.getEmail());

        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No such user"
            );
        }

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid credentials"
            );
        }

        var authentication = user.intoToken();

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new UserInfoDto(user);
    }

    public void logout() {
        SecurityContextHolder.clearContext();
    }

    public void register(UserRegisterDto registerDto, User.Role role) throws IOException {
        var token = generator.generate();

        var user = new User()
                .setEmail(registerDto.getEmail())
                .setUsername(registerDto.getUsername())
                .setPassword(
                        passwordEncoder.encode(registerDto.getPassword())
                )
                .setVerificationToken(token)
                .setRole(role);

        mailService.sendSimpleMail(
                validationMail(templateEngine, user, token)
                .setRecipient(user.getEmail())
        );

        repository.save(user);
    }

    public void validation(String email, String token) {
        var user = repository.getUserByEmail(email);
        var dbToken = user.getVerificationToken();

        if (dbToken == null || !dbToken.equals(token))
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "invalid verification attempt"
            );

        user.setVerificationToken(null);
        repository.save(user);
    }

    public MailDto validationMail (SpringTemplateEngine engine, User user, String token) throws IOException {
        final String baseUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .build()
                .toUriString();

        var context = new Context();
        context.setVariable("username", user.getUsername());
        context.setVariable("email", user.getEmail());
        context.setVariable("uri", baseUrl);
        context.setVariable("token", token);

        var resource = ResourceUtils.getFile("classpath:WEB-INF/templates/mail/MailVerification.html");
        var scanner = new Scanner(resource, StandardCharsets.UTF_8);
        var content = scanner.useDelimiter("\\A").next();
        scanner.close();

        var mail = new MailDto();
        mail.setSubject("Finish your registration");
        mail.setBody(engine.process(content, context));
        return mail;
    }

    public void clearUnverified() {
        final long days = 7 * 1000 * 60 * 60 * 24;
        repository.clearUnverified(System.currentTimeMillis() - days);
    }

}
