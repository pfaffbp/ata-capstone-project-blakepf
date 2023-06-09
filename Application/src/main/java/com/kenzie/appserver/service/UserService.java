package com.kenzie.appserver.service;

import com.kenzie.appserver.config.CacheAnimeStore;
import com.kenzie.appserver.config.CacheUserStore;
import com.kenzie.appserver.repositories.UserRepository;
import com.kenzie.appserver.service.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;
    private CacheUserStore cache;

    public UserService(UserRepository userRepository, CacheUserStore cache) {
        this.userRepository = userRepository;
        this.cache = cache;
    }

    public User findUserByUsername(String username) {
        User foundUser = cache.get(username);

        if(foundUser != null) {
            return foundUser;
        }

        User storedUser = userRepository
                .findById(username)
                .map(user -> new User(user.getUsername(), user.getFriends(), user.getPassword(), user.getAge()
                        user.getEmail(), user.getBio(), user.getFavoriteAnime(), user.getFullName()))
                .orElse(null);
        if (storedUser != null) {
            cache.add(storedUser.getUsername(), storedUser);
        }

        return storedUser;
    }

}
