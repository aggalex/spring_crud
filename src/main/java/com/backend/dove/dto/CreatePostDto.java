package com.backend.dove.dto;

import com.backend.dove.entity.Post;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

public class CreatePostDto {

    @NotNull
    @Max(255)
    String title;

    @NotNull
    @Max(10_000)
    String body;

    Long parent;

    public CreatePostDto() {

    }

    public CreatePostDto(Post post) {
        setBody(post.getBody());
        setTitle(post.getTitle());
    }

    public String getTitle() {
        return title;
    }

    public CreatePostDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getBody() {
        return body;
    }

    public CreatePostDto setBody(String body) {
        this.body = body;
        return this;
    }

    public Long getParent() {
        return parent;
    }

    public CreatePostDto setParent(Long parent) {
        this.parent = parent;
        return this;
    }
}
