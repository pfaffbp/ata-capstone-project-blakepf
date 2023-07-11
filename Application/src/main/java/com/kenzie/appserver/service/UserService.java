package com.kenzie.appserver.service;

import com.kenzie.appserver.config.CacheUserStore;
import com.kenzie.appserver.repositories.CatalogRepository;
import com.kenzie.appserver.repositories.UserRepository;
import com.kenzie.appserver.repositories.model.CatalogRecord;
import com.kenzie.appserver.repositories.model.UserRecord;
import com.kenzie.appserver.service.model.User;
import org.springframework.stereotype.Service;

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
    public User addNewUser(User user) {
        UserRecord userRecord = new UserRecord();
        userRecord.setUserId(user.getUserId());
        userRecord.setEmail(user.getEmail());
        userRecord.setFullName(user.getFullName());
        userRecord.setDisplayName(user.getDisplayName());
        userRecord.setAge(user.getAge());
        userRecord.setBio(user.getBio());
        userRepository.save(userRecord);
        return user;
    }

    public void updateUser(User user) {
        userRepository.findByDisplayName(user.getDisplayName()).ifPresent(userRecord -> {
            throw new NullPointerException("User with display name is not found!");
        });

        UserRecord userRecord = userRepository.findById(user.getUserId()).orElse(null);
        if (user.getAge() != userRecord.getAge()) {
            userRecord.setAge(user.getAge());
        }
        if (user.getEmail() != null && !user.getEmail().equals(userRecord.getEmail())) {
            userRecord.setEmail(user.getEmail());
        }
        if (user.getFullName() != null && !user.getFullName().equals(userRecord.getFullName())) {
            userRecord.setFullName(user.getFullName());
        }
        if (user.getDisplayName() != null && !user.getDisplayName().equals(userRecord.getDisplayName())) {
            userRecord.setDisplayName(user.getDisplayName());
        }
        if (user.getBio() != null && !user.getBio().equals(userRecord.getBio())) {
            userRecord.setBio(user.getBio());
        }
        userRepository.save(userRecord);

        cache.evict(user.getFullName());
    }



    public void deleteUser(String displayName) {
        userRepository.deleteById(findUserByName(displayName).getUserId());
        cache.evict(displayName);
    }


    public List<String> addNewFavorite(String displayName, String animeId) {
        UserRecord existingUser = userRepository.findByDisplayName(displayName).orElse(null);
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
    public List<String> removeFavorite(String displayName, String animeId) {
        UserRecord existingUser = userRepository.findByDisplayName(displayName).orElse(null);
        CatalogRecord existingAnime = animeRepository.findById(animeId).orElse(null);

        if (existingUser == null || existingAnime == null) {
            throw new NullPointerException("User or anime not found.");
        }

        if (!existingUser.getFavoriteAnime().contains(animeId)) {
            throw new IllegalArgumentException("Anime is not in user's favorites.");
        }

        existingUser.getFavoriteAnime().remove(animeId);
        userRepository.save(existingUser);
        return existingUser.getFavoriteAnime();
    }

    public List<String> follow(String userDisplayName, String friendDisplayName) {

        if (userDisplayName.equals(friendDisplayName)) {
            throw new IllegalArgumentException("Cannot add oneself as a friend.");
        }

        UserRecord existingUser = userRepository.findByDisplayName(userDisplayName).orElse(null);
        UserRecord existingFriend = userRepository.findByDisplayName(friendDisplayName).orElse(null);

        if (existingUser == null || existingFriend == null) {
            throw new NullPointerException("One or both users do not exist.");
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

        UserRecord existingUser = userRepository.findByDisplayName(userDisplayName).orElse(null);
        UserRecord existingFriend = userRepository.findByDisplayName(friendDisplayName).orElse(null);

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