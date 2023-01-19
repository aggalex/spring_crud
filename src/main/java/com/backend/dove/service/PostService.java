package com.backend.dove.service;

import com.backend.dove.dto.CreatePostDto;
import com.backend.dove.dto.PostDto;
import com.backend.dove.dto.UpdatePostDto;
import com.backend.dove.entity.Post;
import com.backend.dove.entity.User;
import com.backend.dove.repository.PostRepository;
import com.backend.dove.repository.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
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

    public List<PostDto> get(Pageable pageable) {
        return get(Optional.empty(), pageable);
    }

    public List<PostDto> get(Long parentId, Pageable pageable) {
        return get(Optional.ofNullable(parentId), pageable);
    }

    public List<PostDto> get(Optional<Long> parentId, Pageable pageable) {
        var auth = User.Principal.getOptional()
                .flatMap(principal -> userRepository.findById(principal.getId()));

        var parent = parentId.map(id -> repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Parent post not found"
                ))
        );

        return parent.map(post ->
                        auth.map(user -> repository.getPublicComments(post, pageable))
                                .orElseGet(() -> repository.getPublicComments(post, pageable))
                )
                .orElseGet(() ->
                        auth.map((user) -> repository.getVisiblePostsFor(user, pageable))
                                .orElseGet(() -> repository.getPublic(pageable))
                )
                .stream()
                .map(PostDto::new)
                .collect(Collectors.toList());
    }

    public PostDto getById(long id) {
        var post = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Post does not exist"
                ));

        if (post.isPrivate()) {
            var user = userRepository.findById(
                            User.Principal.get().getId()
                    )
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.UNAUTHORIZED,
                            "No such user"
                    ));

            if (post.getPoster().getFriends().contains(user))
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Post is private"
                );
        }

        return new PostDto(post);
    }

    public PostDto create(CreatePostDto createPostDto) {
        var parent = Optional.ofNullable(createPostDto.getParent())
                .map(postId -> repository.findById(postId)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Parent post does not exist"
                    )))
                .orElse(null);

        var user = userRepository.findById(
                User.Principal.get().getId()
        )
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "No such user"
                ));

        var post = repository.save(
                new Post()
                        .setTitle(createPostDto.getTitle())
                        .setBody(createPostDto.getBody())
                        .setParent(parent)
                        .setPoster(user)
        );

        return new PostDto(post);
    }

    public PostDto update(UpdatePostDto postDto) {
        var post = repository.findById(postDto.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Referenced post does not exist"
                ));

        var auth = User.Principal.get();

        if (auth.getAuthorities().contains(User.Role.USER)
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

        return new PostDto(repository.save(post));
    }

    public void delete(long id) {
        var post = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Referenced post does not exist"
                ));

        var auth = User.Principal.get();

        if (auth.getAuthorities().contains(User.Role.USER)
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
