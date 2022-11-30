package com.backend.dove.entity;


import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Entity
@Table(name = "\"User\"")
public class User {

    public enum Role implements GrantedAuthority {
        ADMIN, USER;

        @Override
        public String getAuthority() {
            return this.toString();
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "verification_token")
    private String verificationToken;

    @GeneratedValue
    @Column(name = "created")
    private ZonedDateTime created;

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public Role getRole() {
        return role;
    }

    public User setRole(Role role) {
        this.role = role;
        return this;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public User setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
        return this;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

}
