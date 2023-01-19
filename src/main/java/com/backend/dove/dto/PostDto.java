package com.backend.dove.dto;

import com.backend.dove.entity.Post;
import com.backend.dove.entity.User;

public class PostDto {

    public static class PosterDto {

        String username;

        long id;

        public PosterDto(User user) {
            setUsername(user.getUsername());
            setId(user.getId());
        }

        public String getUsername() {
            return username;
        }

        public PosterDto setUsername(String username) {
            this.username = username;
            return this;
        }

        public long getId() {
            return id;
        }

        public PosterDto setId(long id) {
            this.id = id;
            return this;
        }
    }

    long id;

    String title;

    String body;

    PosterDto poster;

    PostParentDto parent;

    long likes;

    long views;

    boolean deleted;

    public PostDto() {

    }

    public PostDto(Post post) {
        setTitle(post.getTitle());
        setBody(post.getBody());
        setId(post.getId());
        setDeleted(post.isDeleted());
        setPoster(new PosterDto(post.getPoster()));
        if (post.getParent() != null)
            setParent(new PostParentDto(post.getParent()));
        setLikes(post.getLikes());
        setViews(post.getViews());
    }

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

    public PosterDto getPoster() {
        return poster;
    }

    public PostDto setPoster(PosterDto poster) {
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
