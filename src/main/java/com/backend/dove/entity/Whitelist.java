package com.backend.dove.entity;

import javax.persistence.*;

@Entity
@Table(schema = "\"Whitelist\"")
public class Whitelist implements EmailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    public Long getId() {
        return id;
    }

    public Whitelist setId(Long id) {
        this.id = id;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Whitelist setEmail(String email) {
        this.email = email;
        return this;
    }
}
