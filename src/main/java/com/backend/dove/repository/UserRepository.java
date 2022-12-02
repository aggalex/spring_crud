package com.backend.dove.repository;

import com.backend.dove.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.email = ?1")
    User getUserByEmail(String email);

    @Modifying
    @Transactional
    @Query("delete from User u where u.email = ?1")
    void deleteUserByEmail(String email);

    /**
     * Clear all the unverified users before the specified date
     *
     * @param before the date as a unix time stamp
     */
    @Modifying
    @Transactional
    @Query("""
        delete from User u where
            u.verificationToken is not null and
            u.created < ?1
        """)
    void clearUnverified(long before);

    @Query("""
        select f from User u
        join u.friends f
        where u.id = ?1
    """)
    Set<User> getFriends(long userId);

    default Set<User> getFriends(User user) {
        return getFriends(user.getId());
    }

    @Query("""
        select u from User u
        join u.friendRequests fr
        where fr.id = ?1
    """)
    Set<User> getFriendRequestsTowards(long userId);

    default Set<User> getFriendRequestsTowards(User user) {
        return getFriendRequestsTowards(user.getId());
    }

    @Query("""
        select count(b) > 0
        from User u
        join u.blocked b
        where u.id = ?1
        and b.id = ?2
    """)
    boolean isBlockedBy(long selfId, long potentiallyBlockedId);

    default boolean isBlockedBy(User self, User potentiallyBlocked) {
        return isBlockedBy(self.getId(), potentiallyBlocked.getId());
    }

    @Query("""
        select b
        from User u
        join u.blocked b
        where u.id = ?1
    """)
    Set<User> getBlocked(long userId);

    default Set<User> getBlocked(User user) {
        return getBlocked(user.getId());
    }

    default void makeFriends(User user1, User user2) {
        user1.friend(user2);
        save(user1);
        save(user2);
    }
}
