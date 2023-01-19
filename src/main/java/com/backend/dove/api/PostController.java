package com.backend.dove.api;

import com.backend.dove.dto.CreatePostDto;
import com.backend.dove.dto.PostDto;
import com.backend.dove.dto.UpdatePostDto;
import com.backend.dove.service.PostService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController("Post Controller")
@RequestMapping("/api/post")
public class PostController {

    PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    @GetMapping("")
    public List<PostDto> get(@NotNull final Pageable pageable) {
        return service.get(pageable);
    }

    @GetMapping("{id}")
    public PostDto getById(@PathVariable("id") @NotNull final long id) {
        return service.getById(id);
    }

    @GetMapping("{id}/comments")
    public List<PostDto> getCommentsOf(
            @PathVariable("id") @NotNull final long id,
            @NotNull Pageable pageable
    ) {
        return service.get(id, pageable);
    }

    @PostMapping("")
    @ResponseBody
    public PostDto create(@RequestBody @NotNull final CreatePostDto createPostDto) {
        return service.create(createPostDto);
    }

    @PatchMapping("")
    public PostDto update(@RequestBody @NotNull final UpdatePostDto updatePostDto) {
        return service.update(updatePostDto);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") @NotNull final long id) {
        service.delete(id);
    }
}
