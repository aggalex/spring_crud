package com.backend.dove.unit.repository;

import com.backend.dove.entity.Post;
import com.backend.dove.entity.User;
import com.backend.dove.repository.PostRepository;
import com.backend.dove.repository.UserRepository;
import com.backend.dove.util.PasswordGenerator;
import com.backend.dove.util.SetUtils;
import com.github.javafaker.Faker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PostsTests {

    @Autowired
    PasswordGenerator generator;

    @Autowired
    Faker faker;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Test
    public void createPost() {
        var poster = userRepository.save(
                new User()
                        .randomise(generator, faker)
        );

        try {
            var post = postRepository.save(
                    new Post()
                            .randomise(faker)
                            .setPoster(poster)
            );

            assertEquals(poster.getId(), post.getPoster().getId());

            postRepository.delete(post);
        } finally {
            userRepository.delete(poster);
        }
    }

    @Test
    public void likePost () {
        var poster = userRepository.save(
                new User()
                        .randomise(generator, faker)
        );

        var liker = userRepository.save(
                new User()
                        .randomise(generator, faker)
        );

        try {
            var post = postRepository.save(
                    new Post()
                            .randomise(faker)
                            .setPoster(poster)
            );

            var likes = post.getId();

            post.addLike(liker);
            postRepository.save(post);

            var newLikes = postRepository.findById(post.getId())
                    .map(Post::getLikes)
                    .orElse(-1L);

            assertEquals(poster.getId(), post.getPoster().getId());

            postRepository.delete(post);
        } finally {
            userRepository.delete(liker);
        }
    }

    @Test
    public void createCommentPost () {
        var poster = userRepository.save(
                new User()
                .randomise(generator, faker)
        );

        var commenter = userRepository.save(
                new User()
                .randomise(generator, faker)
        );

        try {
            var post = postRepository.save(
                    new Post()
                            .randomise(faker)
                            .setPoster(poster)
            );

            var comment = postRepository.save(
                    new Post()
                            .randomise(faker)
                            .setPoster(commenter)
                            .setParent(post)
            );

            System.out.println(post);
            System.out.println(comment);

            try {
                assertEquals(
                        Set.of(comment.getId()),
                        SetUtils.idsOf(post.getComments())
                );
            } finally {
                postRepository.delete(post);
                postRepository.delete(comment);
            }
        } finally {
            userRepository.delete(commenter);
            userRepository.delete(poster);
        }
    }

    @Test
    public void updatePost() {
        var poster = userRepository.save(
                new User()
                        .randomise(generator, faker)
        );

        try {
            var post = postRepository.save(
                    new Post()
                            .randomise(faker)
                            .setPoster(poster)
            );

            var postId = post.getId();
            var posterId = post.getPoster().getId();
            assertEquals(poster.getId(), posterId);

            var newPost = postRepository.save(
                post.randomise(faker)
            );

            assertEquals(postId, newPost.getId());
            assertEquals(posterId, newPost.getPoster().getId());

            postRepository.delete(newPost);
        } finally {
            userRepository.delete(poster);
        }
    }

}
