package com.backend.dove;

import com.backend.dove.repository.UserRepository;
import com.backend.dove.util.PasswordGenerator;
import com.backend.dove.util.annotations.MockUserSecurityContextFactory;
import com.github.javafaker.Faker;
import org.springframework.context.annotation.*;

@Configuration
public class TestConfiguration {

    Faker faker;

    PasswordGenerator generator;

    UserRepository userRepository;

    public TestConfiguration(Faker faker, PasswordGenerator generator, UserRepository userRepository) {
        this.faker = faker;
        this.generator = generator;
        this.userRepository = userRepository;
    }

    @Bean
    MockUserSecurityContextFactory mockUserSecurityContextFactory() {
        return new MockUserSecurityContextFactory(faker, generator, userRepository);
    }
}
