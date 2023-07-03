package com.kenzie.appserver.service;

import com.kenzie.appserver.config.CacheUserStore;
import com.kenzie.appserver.repositories.UserRepository;
import com.kenzie.appserver.repositories.model.UserRecord;
import com.kenzie.appserver.service.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private CacheUserStore cacheUserStore;

    private UserService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new UserService(repository, cacheUserStore);
    }

    @Test
    void findUser_userExists_returnsTrue() {
        //GIVEN
        String userId = randomUUID().toString();
        String email = "randomemail@gmail.com";
        String fullName = "John Doe";
        int age = 27;
        String displayName = "AnimeLover96";
        String bio = "I am a huge lover of all things Anime";
        User user = new User(userId, email, fullName, age, displayName, bio);

        UserRecord userRecord = new UserRecord();
        userRecord.setUserId(user.getUserId());
        userRecord.setEmail(user.getEmail());
        userRecord.setFullName(user.getFullName());
        userRecord.setAge(user.getAge());
        userRecord.setDisplayName(user.getDisplayName());
        userRecord.setBio(user.getBio());
        repository.save(userRecord);

        //WHEN
        when(repository.findById(userId)).thenReturn(Optional.of(userRecord));
        User foundUser = service.findUserByName(displayName);


        //THEN
        assertNotNull(foundUser, "The user was returned");
        assertEquals(userRecord.getUserId(), foundUser.getUserId(), "The userId matches");
        assertEquals(userRecord.getDisplayName(), foundUser.getDisplayName(), "The display names match");
        assertEquals(userRecord.getEmail(), foundUser.getEmail(), "The emails match");
    }

    @Test
    void findById_invalidId_returnsNull() {
        //GIVEN
        String userId = randomUUID().toString();

        //WHEN
        when(repository.findById(userId)).thenReturn(Optional.empty());
        User user = service.findUserByName(userId);

        //THEN
        assertNull(user, "The user is null");
    }
}
