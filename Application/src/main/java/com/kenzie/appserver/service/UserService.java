package com.kenzie.appserver.service;

import com.kenzie.appserver.config.CacheUserStore;
import com.kenzie.appserver.repositories.CatalogRepository;
import com.kenzie.appserver.repositories.UserRepository;
import com.kenzie.appserver.repositories.model.CatalogRecord;
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
                .map(user -> new User(user.getFullName(), user.getAge(), user.getDisplayName(), user.getBio()))
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
            usersList.add(new User(record.getFullName(), record.getAge(), record.getDisplayName(), record.getBio()));
        }

        return usersList;
    }
    public User addNewUser(User user) {
        UserRecord userRecord = new UserRecord();

        userRecord.setFullName(user.getFullName());
        userRecord.setDisplayName(user.getDisplayName());
        userRecord.setAge(user.getAge());
        userRecord.setBio(user.getBio());
        userRepository.save(userRecord);

        return user;
    }
    public void updateUser(User user) {
        if (userRepository.existsById(user.getFullName())) {
            UserRecord userRecord = new UserRecord();

            userRecord.setFullName(user.getFullName());
            userRecord.setDisplayName(user.getDisplayName());
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


    public List<Anime> addNewFavorite(User user, Anime anime) {
        UserRecord existingUser = userRepository.findById(user.getFullName()).orElse(null);
        CatalogRecord existingAnime = animeRepository.findById(anime.getAnimeId()).orElse(null);

        if (existingUser == null || existingAnime == null) {
            throw new IllegalArgumentException("User or anime not found.");
        }

        if (user.getFavoriteAnime().contains(anime)) {
            throw new IllegalArgumentException("Anime is already in user's favorites.");
        }

        user.getFavoriteAnime().add(anime);

        return user.getFavoriteAnime();
    }
    public void removeFavorite(User user, Anime anime) {
        UserRecord existingUser = userRepository.findById(user.getFullName()).orElse(null);
        CatalogRecord existingAnime = animeRepository.findById(anime.getAnimeId()).orElse(null);

        if (existingUser == null || existingAnime == null) {
            throw new IllegalArgumentException("User or anime not found.");
        }

        if (!existingUser.getFavoriteAnime().contains(anime)) {
            throw new IllegalArgumentException("Anime is not in user's favorites.");
        }

        user.getFavoriteAnime().remove(anime);
    }

    public List<User> addFriend(User user, User friend) {
        if (user.getFullName().equals(friend.getFullName())) {
            throw new IllegalArgumentException("Cannot add oneself as a friend.");
        }

        UserRecord existingUser = userRepository.findById(user.getFullName()).orElse(null);
        UserRecord existingFriend = userRepository.findById(friend.getFullName()).orElse(null);

        if (existingUser == null || existingFriend == null) {
            throw new IllegalArgumentException("One or both users do not exist.");
        }

        if (user.getFriends().contains(friend)) {
            throw new IllegalArgumentException("Users are already friends.");
        }

        user.getFriends().add(friend);
        friend.getFriends().add(user);

        return user.getFriends();
    }
    public void removeFriend(User user, User friend) {
        if (user.getFullName().equals(friend.getFullName())) {
            throw new IllegalArgumentException("Cannot remove oneself as a friend.");
        }

        UserRecord existingUser = userRepository.findById(user.getFullName()).orElse(null);
        UserRecord existingFriend = userRepository.findById(friend.getFullName()).orElse(null);

        if (existingUser == null || existingFriend == null) {
            throw new IllegalArgumentException("One or both users do not exist.");
        }

        if (!existingUser.getFriends().contains(friend)) {
            throw new IllegalArgumentException("Users are not friends.");
        }

        existingUser.getFriends().remove(friend);
        existingFriend.getFriends().remove(user);
    }
}
