package com.backend.dove.e2e;

import com.backend.dove.dto.CreatePostDto;
import com.backend.dove.dto.PostDto;
import com.backend.dove.dto.UpdatePostDto;
import com.backend.dove.entity.Post;
import com.backend.dove.entity.User;
import com.backend.dove.util.PasswordGenerator;
import com.backend.dove.util.annotations.LoggedIn;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class PostsTests {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    Faker faker;

    @Autowired
    PasswordGenerator generator;

    @Test
    @LoggedIn
    public void createPublicPosts() throws Exception {
        var dto = new CreatePostDto(new Post().randomise(faker));
        var json = objectMapper.writeValueAsString(dto);
        System.out.println(json);

        this.mvc.perform(
                post("/api/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(dto.getTitle()))
                .andExpect(jsonPath("$.body").value(dto.getBody()))
                .andExpect(jsonPath("$.parent").isEmpty());
    }

    @Test
    @LoggedIn
    public void createPublicComment() throws Exception {
        var result = createAPost();

        var comment = new CreatePostDto(new Post()
                .randomise(faker))
                .setParent(result.getId());
        var commentJson = objectMapper.writeValueAsString(comment);

        this.mvc.perform(
                post("/api/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentJson)
                        .with(csrf())
        )
                .andExpect(status().isOk())
                .andDo(res -> System.out.println(res.getResponse().getContentAsString()))
                .andExpect(jsonPath("$.parent.id").value(result.getId()));
    }

    @Test
    @LoggedIn
    public void updatePost() throws Exception {
        var result = createAPost();

        var post = new UpdatePostDto()
                .setId(result.getId())
                .setBody("New body of post");
        var json = objectMapper.writeValueAsString(post);

        this.mvc.perform(
                patch("/api/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body").value(post.getBody()))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").exists());

    }

    @Test
    @LoggedIn
    public void deletePost() throws Exception {
        var result = createAPost();

        var post = new UpdatePostDto()
                .setId(result.getId())
                .setBody("New body of post");
        var json = objectMapper.writeValueAsString(post);

        this.mvc.perform(
                        delete("/api/post/" + post.getId())
                                .content(json)
                                .with(csrf())
                )
                .andExpect(status().isOk());

        this.mvc.perform(
                        get("/api/post/" + post.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deleted").value(true))
                .andExpect(jsonPath("$.body").value("<Deleted>"))
                .andExpect(jsonPath("$.title").value("<Deleted>"));

    }

    private PostDto createAPost() throws Exception {
        var post = new CreatePostDto(new Post().randomise(faker));
        var postJson = objectMapper.writeValueAsString(post);
        System.out.println(postJson);

        var resultJson = this.mvc.perform(
                        post("/api/post")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(postJson)
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(resultJson, PostDto.class);
    }

}
