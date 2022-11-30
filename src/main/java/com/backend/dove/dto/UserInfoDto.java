package com.backend.dove.dto;

import com.backend.dove.entity.User;

public class UserInfoDto {

    String username;

    String email;

    public UserInfoDto(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
    }

    public String getUsername() {
        return username;
    }

    public UserInfoDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserInfoDto setEmail(String email) {
        this.email = email;
        return this;
    }
}
