package com.backend.dove.repository;

import com.backend.dove.entity.Post;
import com.backend.dove.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

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

}
