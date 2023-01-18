package com.backend.dove.util.annotations;

import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = MockUserSecurityContextFactory.class)
@DirtiesContext
@Transactional
public @interface LoggedIn {
    String user() default Default.STRING;

    String password() default Default.STRING;

    String email() default Default.STRING;
}
