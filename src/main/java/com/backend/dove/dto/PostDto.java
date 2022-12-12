package com.backend.dove.dto;

public class PostDto {

    long id;

    String title;

    String body;

    long poster;

    PostParentDto parent;

    long likes;

    long dislikes;

    long views;

    boolean deleted;

    public long getId() {
        return id;
    }

    public PostDto setId(long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public PostDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getBody() {
        return body;
    }

    public PostDto setBody(String body) {
        this.body = body;
        return this;
    }

    public long getPoster() {
        return poster;
    }

    public PostDto setPoster(long poster) {
        this.poster = poster;
        return this;
    }

    public PostParentDto getParent() {
        return parent;
    }

    public PostDto setParent(PostParentDto parent) {
        this.parent = parent;
        return this;
    }

    public long getLikes() {
        return likes;
    }

    public PostDto setLikes(long likes) {
        this.likes = likes;
        return this;
    }

    public long getDislikes() {
        return dislikes;
    }

    public PostDto setDislikes(long dislikes) {
        this.dislikes = dislikes;
        return this;
    }

    public long getViews() {
        return views;
    }

    public PostDto setViews(long views) {
        this.views = views;
        return this;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public PostDto setDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }
}
