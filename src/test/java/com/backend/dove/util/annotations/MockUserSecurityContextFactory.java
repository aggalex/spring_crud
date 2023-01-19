package com.backend.dove.util.annotations;

import com.backend.dove.entity.User;
import com.backend.dove.repository.UserRepository;
import com.backend.dove.util.PasswordGenerator;
import com.github.javafaker.Faker;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class MockUserSecurityContextFactory implements WithSecurityContextFactory<LoggedIn> {

    Faker faker;

    PasswordGenerator generator;

    UserRepository repository;

    public MockUserSecurityContextFactory(Faker faker, PasswordGenerator generator, UserRepository repository) {
        this.faker = faker;
        this.generator = generator;
        this.repository = repository;
    }

    @Override
    public SecurityContext createSecurityContext(LoggedIn annotation) {
        var context = SecurityContextHolder.createEmptyContext();

        var user = repository.save(
                new User().randomise(generator, faker)
                        .setRole(User.Role.USER)
        );

        context.setAuthentication(user.intoToken());

        return context;
    }
}
