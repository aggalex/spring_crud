package com.backend.dove.entity;


import com.backend.dove.util.PasswordGenerator;
import com.github.javafaker.Faker;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.*;

@Entity
@Table(name = "\"User\"")
public class User implements HasId {

    public enum Role implements GrantedAuthority {
        ADMIN, USER;

        @Override
        public String getAuthority() {
            return this.toString();
        }
    }

    public static class Principal implements UserDetails {
        private String username;
        private String email;
        private String password;
        private Long id;
        private Collection<? extends GrantedAuthority> authorities;

        public static Principal get() {
            return getOptional()
                    .orElse(null);
        }

        public static Optional<Principal> getOptional() {
            return Optional.ofNullable(SecurityContextHolder
                    .getContext()
                    .getAuthentication())
                    .map(auth -> ((Principal) auth.getPrincipal())
                            .setAuthorities(auth.getAuthorities())
                            .setPassword((String) auth.getCredentials()));
        }

        public String getUsername() {
            return username;
        }

        @Override
        public boolean isAccountNonExpired() {
            return false;
        }

        @Override
        public boolean isAccountNonLocked() {
            return false;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return false;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        public Principal setUsername(String username) {
            this.username = username;
            return this;
        }

        public String getEmail() {
            return email;
        }

        public Principal setEmail(String email) {
            this.email = email;
            return this;
        }

        public Long getId() {
            return id;
        }

        public Principal setId(Long id) {
            this.id = id;
            return this;
        }

        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorities;
        }

        @Override
        public String getPassword() {
            return password;
        }

        public Principal setPassword(String password) {
            this.password = password;
            return this;
        }

        public Principal setAuthorities(Collection<? extends GrantedAuthority> authorities) {
            this.authorities = authorities;
            return this;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "verification_token")
    private String verificationToken;

    @GeneratedValue
    @Column(name = "created")
    private ZonedDateTime created;

    @ManyToMany(cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "friend_requests",
            joinColumns = @JoinColumn(name = "self_id"),
            inverseJoinColumns = @JoinColumn(name = "other_id")
    )
    private Set<User> friendRequests = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "friends",
            joinColumns = @JoinColumn(name = "self_id"),
            inverseJoinColumns = @JoinColumn(name = "other_id")
    )
    private Set<User> friends = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "blocked",
            joinColumns = @JoinColumn(name = "self_id"),
            inverseJoinColumns = @JoinColumn(name = "other_id")
    )
    private Set<User> blocked = new HashSet<>();

    @OneToMany(mappedBy = "poster", fetch = FetchType.LAZY)
    private Set<Post> posts = new HashSet<>();

    public User randomise(PasswordGenerator generator, Faker faker) {
        var name = faker.name().username();
        setEmail(name + "@" + faker.internet().domainName());
        setPassword(generator.generate());
        setRole(Role.USER);
        setUsername(name);
        setVerificationToken(null);
        return this;
    }

    public UsernamePasswordAuthenticationToken intoToken() {
        return new UsernamePasswordAuthenticationToken(
                new Principal()
                        .setId(this.id)
                        .setEmail(this.email)
                        .setUsername(this.username),
                this.password,
                List.of(this.role)
        );
    }

    public Long getId() {
        return id;
    }

    public User setId(Long id) {
        this.id = id;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public Role getRole() {
        return role;
    }

    public User setRole(Role role) {
        this.role = role;
        return this;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public User setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
        return this;
    }

    public Set<User> getBlocked() {
        return blocked;
    }

    public User block(User user) {
        this.blocked.add(user);
        return this;
    }

    public User unblock(User user) {
        this.blocked.remove(user);
        return this;
    }

    public Set<User> getFriendRequests() {
        return friendRequests;
    }

    public User friendRequest(User user) {
        this.friendRequests.add(user);
        return this;
    }

    public User undoFriendRequest(User user) {
        this.friendRequests.remove(user);
        return this;
    }

    public Set<User> getFriends() {
        return friends;
    }

    public User friend(User user) {
        this.friends.add(user);
        user.friends.add(this);
        return this;
    }

    public User unfriend(User user) {
        this.friends.remove(user);
        user.friends.remove(this);
        return this;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", verificationToken='" + verificationToken + '\'' +
                ", created=" + created +
                '}';
    }
}
