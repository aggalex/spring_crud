package com.backend.dove.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

public class UpdatePostDto {

    @NotNull
    Long id;

    @Max(255)
    String title;

    @Max(10_000)
    String body;

    public Long getId() {
        return id;
    }

    public UpdatePostDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public UpdatePostDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getBody() {
        return body;
    }

    public UpdatePostDto setBody(String body) {
        this.body = body;
        return this;
    }
}
