package com.backend.dove.repository;

import com.backend.dove.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;

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

}
