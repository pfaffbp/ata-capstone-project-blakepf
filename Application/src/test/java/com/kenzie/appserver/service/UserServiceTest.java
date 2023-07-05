package com.kenzie.appserver.service;

import com.kenzie.appserver.config.CacheUserStore;
import com.kenzie.appserver.repositories.UserRepository;
import com.kenzie.appserver.repositories.model.UserRecord;
import com.kenzie.appserver.service.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

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
        when(repository.findByDisplayName(displayName)).thenReturn(Optional.of(userRecord));
        User foundUser = service.findUserByName(displayName);


        //THEN
        assertNotNull(foundUser, "The user was returned");
        assertEquals(userRecord.getUserId(), foundUser.getUserId(), "The userId matches");
        assertEquals(userRecord.getDisplayName(), foundUser.getDisplayName(), "The display names match");
        assertEquals(userRecord.getEmail(), foundUser.getEmail(), "The emails match");
    }

    @Test
    void findByDisplayName_invalidDisplayName_returnsNull() {
        UserRecord nullRecord = new UserRecord();
        nullRecord.setUserId(null);
        nullRecord.setDisplayName(null);

        when(repository.findByDisplayName(nullRecord.getDisplayName())).thenReturn(Optional.empty());
        User nullUser = service.findUserByName(nullRecord.getDisplayName());

        Assertions.assertNull(nullUser);
    }

    @Test
    void findAllUsers_returnsUsersList() {
        String userId = randomUUID().toString();
        String userId2 = randomUUID().toString();

        UserRecord userRecord = new UserRecord();
        userRecord.setUserId(userId);
        userRecord.setEmail("user1email@gmail.com");
        userRecord.setFullName("Jane Doe");
        userRecord.setAge(27);
        userRecord.setDisplayName("Jane");
        userRecord.setBio("bio of Jane");
        repository.save(userRecord);

        UserRecord userRecord2 = new UserRecord();
        userRecord2.setUserId(userId2);
        userRecord2.setEmail("user2email@gmail.com");
        userRecord2.setFullName("John Doe");
        userRecord2.setAge(27);
        userRecord2.setDisplayName("John");
        userRecord2.setBio("bio of John");
        repository.save(userRecord2);

        List<UserRecord> userRecords = new ArrayList<>();
        userRecords.add(userRecord);
        userRecords.add(userRecord2);

        //WHEN
        when(repository.findAll()).thenReturn(userRecords);
        List<User> users = service.findAllUsers();

        //THEN
        assertNotNull(users, "The users are returned");
        assertEquals(userRecords.size(), users.size(), "The sizes match");
    }

    @Test
    void updateUser_validUser_UpdatesUser(){
        String userId = randomUUID().toString();
        User user = new User(new ArrayList<>(), new ArrayList<>(), "email", userId, new ArrayList<>(),
                "fullName", "displayName", 27, "bio");

        String updatedEmail = "newEmail";

        UserRecord userRecord = new UserRecord();
        userRecord.setUserId(user.getUserId());
        userRecord.setFollowers(user.getFollowers());
        userRecord.setFollowing(user.getFollowing());
        userRecord.setFavoriteAnime(user.getFavoriteAnime());
        userRecord.setEmail(user.getEmail());
        userRecord.setFullName(user.getFullName());
        userRecord.setAge(user.getAge());
        userRecord.setDisplayName(user.getDisplayName());
        userRecord.setBio(user.getBio());
        repository.save(userRecord);


    }


}
