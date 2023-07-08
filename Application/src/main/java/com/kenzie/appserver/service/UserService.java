package com.kenzie.appserver.service;

import com.kenzie.appserver.config.CacheUserStore;
import com.kenzie.appserver.repositories.CatalogRepository;
import com.kenzie.appserver.repositories.UserRepository;
import com.kenzie.appserver.repositories.model.CatalogRecord;
import com.kenzie.appserver.repositories.model.UserRecord;
import com.kenzie.appserver.service.model.User;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    private CacheUserStore cache;
    private CatalogRepository animeRepository;

    public UserService(UserRepository userRepository, CacheUserStore cache, CatalogRepository animeRepository) {
        this.userRepository = userRepository;
        this.cache = cache;
        this.animeRepository = animeRepository;
    }
    public User findUserByName(String displayName) {
        User foundUser = cache.get(displayName);

        if(foundUser != null) {
            return foundUser;
        }
        User storedUser = userRepository
                .findByDisplayName(displayName)
                .map(user -> new User(user.getFollowers(), user.getFollowing(), user.getEmail(), user.getUserId(), user.getFavoriteAnime(), user.getFullName(), user.getDisplayName(), user.getAge(), user.getBio()))
                .orElse(null);
        if (storedUser != null) {
            cache.add(storedUser.getDisplayName(), storedUser);
        }
        return storedUser;
    }

    public List<User> findAllUsers() {
        List<User> usersList = new ArrayList<>();

        Iterable<UserRecord> userIterator = userRepository.findAll();

        for(UserRecord record : userIterator) {
            usersList.add(new User(record.getFollowers(), record.getFollowing(), record.getEmail(), record.getUserId(), record.getFavoriteAnime(), record.getFullName(), record.getDisplayName(), record.getAge(), record.getBio()));
        }

        return usersList;
    }
    public void addNewUser(User user) {
        UserRecord userRecord = new UserRecord();
        userRecord.setUserId(user.getUserId());
        userRecord.setEmail(user.getEmail());
        userRecord.setFullName(user.getFullName());
        userRecord.setDisplayName(user.getDisplayName());
        userRecord.setAge(user.getAge());
        userRecord.setBio(user.getBio());
        userRepository.save(userRecord);
    }

    public void updateUser(User user) {
        if (userRepository.existsById(user.getDisplayName())) {
            UserRecord userRecord = new UserRecord();
            userRecord.setUserId(user.getUserId());
            userRecord.setEmail(user.getEmail());
            userRecord.setFullName(user.getFullName());
            userRecord.setDisplayName(user.getDisplayName());
            userRecord.setAge(user.getAge());
            userRecord.setBio(user.getBio());
            userRepository.save(userRecord);

            cache.evict(user.getFullName());
        }
    }
    public void deleteUser(String displayName) {
        userRepository.deleteById(findUserByName(displayName).getUserId());
        cache.evict(displayName);
    }


    public List<String> addNewFavorite(String displayName, String animeId) {
        UserRecord existingUser = userRepository.findById(findUserByName(displayName).getUserId()).orElse(null);
        CatalogRecord existingAnime = animeRepository.findById(animeId).orElse(null);

        if (existingUser == null || existingAnime == null) {
            throw new IllegalArgumentException("User or anime not found.");
        }

//        if (existingUser.getFavoriteAnime().contains(animeId)) {
//            throw new IllegalArgumentException("Anime is already in user's favorites.");
//        }

        if (existingUser.getFavoriteAnime() == null) {
            List<String> favorites = new ArrayList<>();
            favorites.add(animeId);
            existingUser.setFavoriteAnime(favorites);

            userRepository.save(existingUser);
        } else {
            existingUser.getFavoriteAnime().add(animeId);

            userRepository.save(existingUser);
        }

        return existingUser.getFavoriteAnime();
    }
    public void removeFavorite(String displayName, String animeId) {
        UserRecord existingUser = userRepository.findById(findUserByName(displayName).getUserId()).orElse(null);
        CatalogRecord existingAnime = animeRepository.findById(animeId).orElse(null);

        if (existingUser == null || existingAnime == null) {
            throw new IllegalArgumentException("User or anime not found.");
        }


        existingUser.getFavoriteAnime().remove(animeId);
        userRepository.save(existingUser);
    }

    public List<String> follow(String userDisplayName, String friendDisplayName) {
        if (userDisplayName.equals(friendDisplayName)) {
            throw new IllegalArgumentException("Cannot add oneself as a friend.");
        }

        UserRecord existingUser = userRepository.findById(findUserByName(userDisplayName).getUserId()).orElse(null);
        UserRecord existingFriend = userRepository.findById(findUserByName(friendDisplayName).getUserId()).orElse(null);

        if (existingUser == null || existingFriend == null) {
            throw new IllegalArgumentException("One or both users do not exist.");
        }

        if (existingUser.getFollowing() == null) {
            List<String> following = new ArrayList<>();
            following.add(existingFriend.getDisplayName());
            existingUser.setFollowing(following);
            userRepository.save(existingUser);
        } else {
            existingUser.getFollowing().add(existingFriend.getDisplayName());
            userRepository.save(existingUser);
        }

        if (existingFriend.getFollowers() == null) {
            List<String> followers = new ArrayList<>();
            followers.add(existingUser.getDisplayName());
            existingFriend.setFollowers(followers);
            userRepository.save(existingFriend);
        } else {
            existingFriend.getFollowers().add(existingUser.getDisplayName());
            userRepository.save(existingFriend);
        }


        return existingUser.getFollowing();
    }

    public void unfollow(String userDisplayName, String friendDisplayName) {
        if (userDisplayName.equals(friendDisplayName)) {
            throw new IllegalArgumentException("Cannot remove oneself as a friend.");
        }

        UserRecord existingUser = userRepository.findById(findUserByName(userDisplayName).getUserId()).orElse(null);
        UserRecord existingFriend = userRepository.findById(findUserByName(friendDisplayName).getUserId()).orElse(null);

        if (existingUser == null || existingFriend == null) {
            throw new IllegalArgumentException("One or both users do not exist.");
        }

        existingUser.getFollowing().remove(friendDisplayName);
        existingFriend.getFollowers().remove(userDisplayName);

        userRepository.save(existingUser);
        userRepository.save(existingFriend);
    }

    public String findDisplayNameByEmail(String email) {
        Optional<UserRecord> record = userRepository.findByEmail(email);
        if (record.isPresent()) {
            return record.get().getDisplayName();
        } else {
            return null;
        }
    }

}