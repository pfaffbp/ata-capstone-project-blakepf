package com.kenzie.appserver.service;

import com.kenzie.appserver.config.CacheStoreUser;
import com.kenzie.appserver.repositories.UserRepository;
import com.kenzie.appserver.repositories.model.UserRecord;
import com.kenzie.appserver.service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public CacheStoreUser cache;

    @Autowired
    public UserService(UserRepository userRepository, CacheStoreUser cache) {
        this.userRepository = userRepository;
        this.cache = cache;
    }

    public User findUserById(String id) {

        User cachedUser = cache.get(id);
        if (cachedUser != null) {
            return cachedUser;
        }

        User userFromService = userRepository
                .findById(id)
                .map(users -> new User(users.getUserId(),
                        users.getName(),
                        users.getEmail(),
                        users.getUsername(),
                        users.getBirthday()))
                .orElse(null);

        if (userFromService != null) {
            cache.add(userFromService.getUserId(), userFromService);
        }

        return userFromService;
    }

    public User findUserByName(String names) {
        User nameFromService = userRepository
                .findById(names)
                .map(users -> new User(users.getName(),
                        users.getUserId(),
                        users.getEmail(),
                        users.getUsername(),
                        users.getBirthday()))
                .orElse(null);

        return nameFromService;
    }

    public User addNewUser(User user) {
        UserRecord userRecord = new UserRecord();

        userRecord.setUserId(user.getUserId());
        userRecord.setName(user.getName());
        userRecord.setUsername(user.getUsername());
        userRecord.setEmail(user.getEmail());
        userRecord.setBirthday(user.getBirthday());
        userRepository.save(userRecord);

        if (userRecord != null) {
            cache.add(userRecord.getUserId(), user);
        }
        return user;
    }

    public List<User> findAllUsers() {
        List<User> users = new ArrayList<>();

        Iterable<UserRecord> userIterator = userRepository.findAll();
        for (UserRecord record : userIterator) {
            users.add(new User(record.getUserId(),
                    record.getName(),
                    record.getEmail(),
                    record.getUsername(),
                    record.getBirthday()));
        }
        return users;
    }

    public void updateUser(User user) {
        if (userRepository.existsById(user.getUserId())) {
            UserRecord userRecord = new UserRecord();
            userRecord.setUserId(user.getUserId());
            userRecord.setUsername(user.getUsername());
            userRecord.setName(user.getName());
            userRecord.setEmail(user.getEmail());
            userRecord.setBirthday(user.getBirthday());
            userRepository.save(userRecord);
        }

        User userFromCache = null;
        userFromCache = cache.get(user.getUserId());
        if (userFromCache != null) {
            cache.evict(userFromCache.getUserId());
        }

    }

    public void deleteUserById(String userID) {
        userRepository.deleteById(userID);
        User user = null;
        if (userID != null) {
            user = cache.get(userID);
        } else {
            throw new IllegalArgumentException("User ID not valid.");
        }
        if (user != null) {
            cache.evict(user.getUserId());
        }
    }
}

