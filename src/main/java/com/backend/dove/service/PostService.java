package com.backend.dove.service;

import com.backend.dove.dto.CreatePostDto;
import com.backend.dove.dto.UpdatePostDto;
import com.backend.dove.entity.Post;
import com.backend.dove.entity.User;
import com.backend.dove.repository.PostRepository;
import com.backend.dove.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Optional;

public class PostService {

    PostRepository repository;

    UserRepository userRepository;

    public PostService(
            PostRepository repository,
            UserRepository userRepository
    ) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public void create(CreatePostDto createPostDto) {
        var parent = Optional.ofNullable(createPostDto.getParent())
                .map(postId -> repository.findById(postId)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Parent post does not exist"
                    )))
                .orElse(null);

        var post = new Post()
                .setTitle(createPostDto.getTitle())
                .setBody(createPostDto.getBody())
                .setParent(parent)
                .setPoster((User) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getCredentials());

        repository.save(post);
    }

    public void update(UpdatePostDto postDto) {
        var post = repository.findById(postDto.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Referenced post does not exist"
                ));

        var auth = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getCredentials();

        if (auth.getRole() == User.Role.USER
                && !Objects.equals(post.getPoster().getId(), auth.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Post doesn't belong to you"
            );
        }

        Optional.ofNullable(postDto.getTitle())
                .map(post::setTitle);
        Optional.ofNullable(postDto.getBody())
                .map(post::setBody);
    }

    public void delete(long id) {
        var post = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Referenced post does not exist"
                ));

        var auth = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getCredentials();

        if (auth.getRole() == User.Role.USER
                && !Objects.equals(post.getPoster().getId(), auth.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Post doesn't belong to you"
            );
        }

        post.setDeleted(true)
                .setBody("<Deleted>")
                .setTitle("<Deleted>");

        repository.save(post);
    }

}
