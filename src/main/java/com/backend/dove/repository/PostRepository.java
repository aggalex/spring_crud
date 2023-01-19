package com.backend.dove.repository;

import com.backend.dove.dto.PostDto;
import com.backend.dove.entity.Post;
import com.backend.dove.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("""
            select p from Post p
            join p.poster.friends f
            where p.privatePost = false or f.id = ?1
            """)
    List<Post> getVisiblePostsFor(long userId, Pageable pageable);

    default List<Post> getVisiblePostsFor(User user, Pageable pageable) {
        return getVisiblePostsFor(user.getId(), pageable);
    }

    @Query("""
            select p from Post p
            join p.poster.friends f
            where ((p.privatePost = false) or (f.id = ?1)) and p.parent.id = ?2
            """)
    List<Post> getVisibleCommentsFor(
            long userId,
            long postId,
            Pageable pageable
    );

    default List<Post> getVisibleCommentsFor(User user, Post post, Pageable pageable) {
        return getVisibleCommentsFor(user.getId(), post.getId(), pageable);
    }

    @Query("""
        select p from Post p
        where p.privatePost = false
    """)
    List<Post> getPublic(Pageable pageable);

    @Query("""
        select p from Post p
        where p.privatePost = false and p.parent.id = ?1
    """)
    List<Post> getPublicComments(long postId, Pageable pageable);

    default List<Post> getPublicComments(Post post, Pageable pageable) {
        return getPublicComments(post.getId(), pageable);
    }
}
