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

    public UserService(UserRepository userRepository, CacheUserStore cache) {
        this.userRepository = userRepository;
        this.cache = cache;
    }
    public User findUserByName(String displayName) {
        User foundUser = cache.get(displayName);

        if(foundUser != null) {
            return foundUser;
        }
        User storedUser = userRepository
                .findByDisplayName(displayName)
                .map(user -> new User(user.getUserId(), user.getEmail(), user.getFullName(),
                        user.getAge(), user.getDisplayName(), user.getBio()))
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
            usersList.add(new User(record.getUserId(), record.getEmail(), record.getFullName(), record.getAge(),
                    record.getDisplayName(), record.getBio()));
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
        userRepository.deleteById(displayName);
        cache.evict(displayName);
    }


    public List<String> addNewFavorite(String displayName, String animeId) {
        UserRecord existingUser = userRepository.findById(displayName).orElse(null);
        CatalogRecord existingAnime = animeRepository.findById(animeId).orElse(null);

        if (existingUser == null || existingAnime == null) {
            throw new IllegalArgumentException("User or anime not found.");
        }

        if (existingUser.getFavoriteAnime().contains(animeId)) {
            throw new IllegalArgumentException("Anime is already in user's favorites.");
        }

        existingUser.getFavoriteAnime().add(animeId);

        return existingUser.getFavoriteAnime();
    }
    public void removeFavorite(String displayName, String animeId) {
        UserRecord existingUser = userRepository.findById(displayName).orElse(null);
        CatalogRecord existingAnime = animeRepository.findById(animeId).orElse(null);

        if (existingUser == null || existingAnime == null) {
            throw new IllegalArgumentException("User or anime not found.");
        }

        if (!existingUser.getFavoriteAnime().contains(animeId)) {
            throw new IllegalArgumentException("Anime is not in user's favorites.");
        }

        existingUser.getFavoriteAnime().remove(animeId);
    }

    public List<String> addFriend(String userDisplayName, String friendDisplayName) {
        if (userDisplayName.equals(friendDisplayName)) {
            throw new IllegalArgumentException("Cannot add oneself as a friend.");
        }

        UserRecord existingUser = userRepository.findById(userDisplayName).orElse(null);
        UserRecord existingFriend = userRepository.findById(friendDisplayName).orElse(null);

        if (existingUser == null || existingFriend == null) {
            throw new IllegalArgumentException("One or both users do not exist.");
        }

        if (existingUser.getFriends().contains(existingFriend.getDisplayName())) {
            throw new IllegalArgumentException("Users are already friends.");
        }

        existingUser.getFriends().add(friendDisplayName);
        existingFriend.getFriends().add(userDisplayName);

        return existingUser.getFriends();
    }
    public void removeFriend(String userDisplayName, String friendDisplayName) {
        if (userDisplayName.equals(friendDisplayName)) {
            throw new IllegalArgumentException("Cannot remove oneself as a friend.");
        }

        UserRecord existingUser = userRepository.findById(userDisplayName).orElse(null);
        UserRecord existingFriend = userRepository.findById(friendDisplayName).orElse(null);

        if (existingUser == null || existingFriend == null) {
            throw new IllegalArgumentException("One or both users do not exist.");
        }

        if (!existingUser.getFriends().contains(friendDisplayName)) {
            throw new IllegalArgumentException("Users are not friends.");
        }

        existingUser.getFriends().remove(friendDisplayName);
        existingFriend.getFriends().remove(userDisplayName);
    }

 /*   public String checkNicknameUniqueness(String nickname) {
        Optional<UserRecord> userRecord = userRepository.findByDisplayName(nickname);
        if (userRecord.isPresent()) {
            return userRecord.get().getDisplayName();
        } else {
            return null;
        }
    }*/

    public boolean checkNicknameUniqueness(String nickname) {
        Optional<UserRecord> userRecord = userRepository.findByDisplayName(nickname);
        return !userRecord.isPresent();
    }

}
