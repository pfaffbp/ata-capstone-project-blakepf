package com.kenzie.appserver.service;

import com.kenzie.appserver.config.CacheUserStore;
import com.kenzie.appserver.repositories.CatalogRepository;
import com.kenzie.appserver.repositories.UserRepository;
import com.kenzie.appserver.repositories.model.UserRecord;
import com.kenzie.appserver.service.model.Anime;
import com.kenzie.appserver.service.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;
    private CacheUserStore cache;

    private CatalogRepository animeRepository;

    public UserService(UserRepository userRepository, CacheUserStore cache) {
        this.userRepository = userRepository;
        this.cache = cache;
    }
    public User findUserByName(String fullName) {
        User foundUser = cache.get(fullName);

        if(foundUser != null) {
            return foundUser;
        }
        User storedUser = userRepository
                .findById(fullName)
                .map(user -> new User(user.getFullName(), user.getAge(), user.getBio()))
                .orElse(null);
        if (storedUser != null) {
            cache.add(storedUser.getFullName(), storedUser);
        }
        return storedUser;
    }
    public List<User> findAllUsers() {
        List<User> usersList = new ArrayList<>();

        Iterable<UserRecord> userIterator = userRepository.findAll();

        for(UserRecord record : userIterator) {
            usersList.add(new User(record.getFullName(), record.getAge(), record.getBio()));
        }

        return usersList;
    }
    public User addNewUser(User user) {
        UserRecord userRecord = new UserRecord();

        userRecord.setFullName(user.getFullName());
        userRecord.setAge(user.getAge());
        userRecord.setBio(user.getBio());
        userRepository.save(userRecord);

        return user;
    }
    public void updateUser(User user) {
        if (userRepository.existsById(user.getFullName())) {
            UserRecord userRecord = new UserRecord();

            userRecord.setFullName(user.getFullName());
            userRecord.setAge(user.getAge());
            userRecord.setBio(user.getBio());
            userRepository.save(userRecord);

            cache.evict(user.getFullName());
        }
    }
    public void deleteUser(String fullName) {
        userRepository.deleteById(fullName);
        cache.evict(fullName);
    }
    //starting method for adding a new favorite anime to a specific user
//    public List<Anime> addNewFavorite(User user, Anime anime) {
//
//    }
}
