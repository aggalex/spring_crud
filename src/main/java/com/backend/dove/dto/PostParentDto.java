package com.backend.dove.dto;

public class PostParentDto {

    long id;

    String title;

    String body;

    public long getId() {
        return id;
    }

    public PostParentDto setId(long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public PostParentDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getBody() {
        return body;
    }

    public PostParentDto setBody(String body) {
        this.body = body;
        return this;
    }
}
