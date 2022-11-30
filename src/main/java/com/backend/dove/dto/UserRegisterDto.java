package com.backend.dove.dto;

import javax.validation.constraints.*;

public class UserRegisterDto {

    @NotBlank
    @Max(500)
    @Min(8)
    String password;

    @NotBlank
    @Max(255)
    String email;

    @NotBlank
    @Max(255)
    String username;

    public String getUsername() {
        return username;
    }

    public UserRegisterDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserRegisterDto setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserRegisterDto setEmail(String email) {
        this.email = email;
        return this;
    }

    /**
     * Translates UserRegisterDto to UserLoginDto for use in Tests
     *
     * @return the UserLoginDto with the same data
     */
    public UserLoginDto toLogin() {
        return new UserLoginDto()
                .setEmail(getEmail())
                .setPassword(getPassword());
    }
}
