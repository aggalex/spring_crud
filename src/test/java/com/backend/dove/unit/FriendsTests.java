package com.backend.dove.unit;

import com.backend.dove.entity.User;
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

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FriendsTests {

    @Autowired
    UserRepository repository;

    @Autowired
    Faker faker;

    @Autowired
    PasswordGenerator generator;

    @Test
    public void testFriendRequests() throws Exception {
        var receiver = new User()
                .randomise(generator, faker);

        var requester = new User()
                .randomise(generator, faker)
                .friendRequest(receiver);

        repository.save(receiver);
        repository.save(requester);

        try {
            var requests = repository.getFriendRequestsTowards(receiver);
            assertEquals("friend requests list is invalid",
                    Set.of(requester.getId()),
                    SetUtils.idsOf(requests));

            requester.undoFriendRequest(receiver);
            repository.save(requester);
            var newRequests = repository.getFriendRequestsTowards(receiver);
            assertEquals("undo of friend request failed",
                    Set.of(),
                    SetUtils.idsOf(newRequests));
        } finally {
            repository.delete(requester);
            repository.delete(receiver);
        }
    }

    @Test
    public void testFriends() throws Exception {
        var requester = repository.save(
                new User()
                .randomise(generator, faker)
        );

        var receiver = repository.save(
                new User()
                .randomise(generator, faker)
        );

        repository.makeFriends(requester, receiver);

        try {
            assertEquals("Receiver's friends list is invalid",
                    Set.of(requester.getId()),
                    SetUtils.idsOf(receiver.getFriends()));
            assertEquals("Requesters's friends list is invalid",
                    Set.of(receiver.getId()),
                    SetUtils.idsOf(requester.getFriends()));

            receiver.unfriend(requester);
            repository.save(requester);
            repository.save(receiver);

            assertEquals("Receiver's unfriend failed",
                    Set.of(),
                    SetUtils.idsOf(receiver.getFriends()));
            assertEquals("Requesters's unfriend failed",
                    Set.of(),
                    SetUtils.idsOf(requester.getFriends()));
        } finally {
            repository.delete(receiver);
            repository.delete(requester);
        }
    }

    @Test
    public void testBlocked() throws Exception {
        var blocked = repository.save(
                new User()
                .randomise(generator, faker)
        );

        var blocker = repository.save(
                new User()
                .randomise(generator, faker)
                .block(blocked)
        );

        try {
            var blockedSet = blocker.getBlocked();
            assertTrue("Block not found", repository.isBlockedBy(blocker, blocked));
            assertEquals("Blocked users list is invalid",
                    Set.of(blocked.getId()),
                    SetUtils.idsOf(blockedSet));

            blocker.unblock(blocked);
            repository.save(blocker);
            var newBlockedSet = blocker.getBlocked();
            assertTrue("Block persisted after unblock", !repository.isBlockedBy(blocker, blocked));
        } finally {
            repository.delete(blocker);
            repository.delete(blocked);
        }
    }

}
