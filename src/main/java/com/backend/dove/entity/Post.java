package com.backend.dove.entity;

import com.github.javafaker.Faker;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

@Entity
@Table(name = "\"Posts\"")
public class Post implements HasId {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "body", nullable = false)
    private String body;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    /**
     * Poster column, that takes the following values:
     * - ID of OP that posted this post
     * - Null if OP was deleted.
     */
    @ManyToOne
    @JoinColumn(name = "poster", updatable = false)
    private User poster;

    @ManyToOne
    @JoinColumn(name = "parent", referencedColumnName = "id", updatable = false)
    private Post parent;

    @ManyToMany(cascade = {CascadeType.REMOVE}, fetch=FetchType.EAGER)
    @JoinTable(
            name = "likes",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> likedBy = new HashSet<>();

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private Set<Post> comments = new HashSet<>();

    @Column(name = "views")
    private long views = 0;

    @Column(name = "private")
    private boolean privatePost;

    public Post randomise(Faker faker) {
        final Set<Supplier<String>> topics = Set.of(
                () -> "My favourite beer, " + faker.beer().name(),
                () -> "Anyone else thinking " + faker.book().author() + " is an awesome author?",
                () -> "Fun website I made: " + faker.internet().url(),
                () -> "Say hello to my " + faker.cat().breed() + ", " + faker.cat().name() + "!",
                () -> faker.chuckNorris().fact(),
                () -> "Sign up immediately to our new course \"" + faker.educator().course() + "\" at " + faker.internet().url() + "!",
                () -> faker.gameOfThrones().character() + " in " + faker.gameOfThrones().city() + "??? O.o",
                () -> "Renouned hacker gang called \"" + faker.hacker().adjective() + "\" attacked " + faker.internet().domainName() + " in " + faker.date().toString(),
                () -> "Anyone else love playing " + faker.music().instrument() + "?",
                () -> "My favourite pokemon is " + faker.pokemon() + ", what's yours?",
                () -> faker.shakespeare().hamletQuote(),
                () -> faker.space().agency() + " made a groundbreaking discovery about a new phenomenon on the moon" + faker.space().moon()
        );
        setTitle(topics.stream()
                .skip((long) (topics.size() * Math.random()))
                .findFirst()
                .orElse(topics.iterator().next())
                .get());
        setBody(String.join("\n", faker.lorem().paragraphs(5)));
        return this;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
//                ", body='" + body + '\'' +
                ", deleted=" + deleted +
                ", poster=" + poster +
                ", parent=" + Optional.ofNullable(parent)
                    .map(Post::getId)
                    .orElse(null) +
                ", comments=" + comments +
                ", views=" + views +
                '}';
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Post setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getBody() {
        return body;
    }

    public Post setBody(String body) {
        this.body = body;
        return this;
    }

    public User getPoster() {
        return poster;
    }

    public Post setPoster(User poster) {
        this.poster = poster;
        return this;
    }

    public Post getParent() {
        return parent;
    }

    public Post setParent(Post parent) {
        if (parent == null)
            return this;

        this.parent = parent;
        parent.addComment(this);
        return this;
    }

    public Set<Post> getComments() {
        return comments;
    }

    public Post addComment(Post comment) {
        if (!Objects.equals(comment.getParent().getId(), this.getId())) {
            parent.setParent(this); // This calls addComment anyway
            return this;
        }

        this.comments.add(comment);
        return this;
    }

    public long getLikes() {
        return this.likedBy.size();
    }

    public void addLike(User user) {
        this.likedBy.add(user);
    }

    public long getViews() {
        return views;
    }

    public Post setViews(long views) {
        this.views = views;
        return this;
    }

    public Post view() {
        this.setViews(this.getViews() + 1);
        return this;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public Post setDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public boolean isPrivate() {
        return privatePost;
    }

    public Post setPrivate(boolean privatePost) {
        this.privatePost = privatePost;
        return this;
    }
}
