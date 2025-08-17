package com.backend.dove.service;


import com.backend.dove.dto.*;
import com.backend.dove.entity.User;
import com.backend.dove.repository.BlacklistRepository;
import com.backend.dove.repository.UserRepository;
import com.backend.dove.repository.WhitelistRepository;
import com.backend.dove.util.PasswordGenerator;
import org.springframework.beans.factory.annotation.Value;
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

    private WhitelistRepository whitelistRepository;

    private BlacklistRepository blacklistRepository;

    private PasswordEncoder passwordEncoder;

    private MailService mailService;

    private PasswordGenerator generator;

    private SpringTemplateEngine templateEngine;

    @Value("${registration.require-verification-mail}")
    private boolean requireVerificationMail;

    @Value("${registration.enable-whitelist}")
    private boolean whitelistIsEnabled;


    public UserService(UserRepository repository,
                       WhitelistRepository whitelistRepository,
                       BlacklistRepository blacklistRepository,
                       PasswordEncoder passwordEncoder,
                       MailService mailService,
                       PasswordGenerator generator,
                       SpringTemplateEngine templateEngine) {
        this.repository = repository;
        this.whitelistRepository = whitelistRepository;
        this.blacklistRepository = blacklistRepository;
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

        if (user.getVerificationToken() != null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Please verify your email before logging in for the first time"
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
        var token = requireVerificationMail? generator.generate(): null;

        if (blacklistRepository.existsByEmail(registerDto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email is blacklisted");
        }

        if (whitelistIsEnabled && !whitelistRepository.existsByEmail(registerDto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email is not whitelisted");
        }

        var user = new User()
                .setEmail(registerDto.getEmail())
                .setUsername(registerDto.getUsername())
                .setPassword(
                        passwordEncoder.encode(registerDto.getPassword())
                )
                .setVerificationToken(token)
                .setRole(role);

        if (requireVerificationMail) {
            mailService.sendSimpleMail(
                    validationMail(templateEngine, user, token)
                            .setRecipient(user.getEmail())
            );
        }

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

    public UserInfoDto info() {
        var principal = User.Principal.getOptional()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Not logged in"
                ));

        var user = repository.findById(principal.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Logged in user is a ghost"
                ));

        return new UserInfoDto(user);
    }
}
