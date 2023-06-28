package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.UserCreateRequest;
import com.kenzie.appserver.controller.model.UserResponse;
import com.kenzie.appserver.controller.model.UserUpdateRequest;
import com.kenzie.appserver.service.CatalogService;
import com.kenzie.appserver.service.UserService;
import com.kenzie.appserver.service.model.Anime;
import com.kenzie.appserver.service.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private CatalogService animeService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{fullName}")
    public ResponseEntity<UserResponse> searchByFullName(@PathVariable("fullName") String fullName) {
        User user = userService.findUserByName(fullName);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        UserResponse response = createUserResponse(user);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> userList = userService.findAllUsers();
        if (userList == null ||  userList.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        List<UserResponse> response = new ArrayList<>();

        for (User users : userList) {
            response.add(this.createUserResponse(users));
        }

        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserUpdateRequest request) {
        User user = new User(request.getFullName(), request.getAge(), request.getFullName(), request.getBio());

        userService.updateUser(user);

        UserResponse response = createUserResponse(user);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<UserResponse> addNewUser(@RequestBody UserCreateRequest request) {
        User user = new User(request.getFullName(), request.getAge(), request.getDisplayName(), request.getBio());
        userService.addNewUser(user);

        UserResponse response = createUserResponse(user);

        return ResponseEntity.created(URI.create("/user/" + response.getFullName())).body(response);
    }

    @DeleteMapping("/{fullName}")
    public ResponseEntity deleteUserByFullName(@PathVariable("fullName") String fullName) {
        userService.deleteUser(fullName);

        return ResponseEntity.status(204).build();
    }

    @PostMapping("/{fullName}/friend/{friendFullName}")
    public ResponseEntity<UserResponse> addFriend(
            @PathVariable("fullName") String fullName,
            @PathVariable("friendFullName") String friendFullName
    ) {
        User user = userService.findUserByName(fullName);
        User friend = userService.findUserByName(friendFullName);

        if (user == null || friend == null) {
            return ResponseEntity.notFound().build();
        }

        List<User> friends = userService.addFriend(user, friend);

        UserResponse response = createUserResponse(user);
        response.setFriends(friends);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{fullName}/friend/{friendFullName}")
    public ResponseEntity<UserResponse> removeFriend(
            @PathVariable("fullName") String fullName,
            @PathVariable("friendFullName") String friendFullName
    ) {
        User user = userService.findUserByName(fullName);
        User friend = userService.findUserByName(friendFullName);

        if (user == null || friend == null) {
            return ResponseEntity.notFound().build();
        }

        userService.removeFriend(user, friend);

        UserResponse response = createUserResponse(user);
        response.setFriends(user.getFriends());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{fullName}/favorite")
    public ResponseEntity<UserResponse> addFavorite(
            @PathVariable("fullName") String fullName,
            @RequestBody Anime anime
    ) {
        User user = userService.findUserByName(fullName);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        List<Anime> favoriteAnime = userService.addNewFavorite(user, anime);

        UserResponse response = createUserResponse(user);
        response.setFavoriteAnime(favoriteAnime);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{fullName}/favorite/{animeId}")
    public ResponseEntity<UserResponse> removeFavorite(
            @PathVariable("fullName") String fullName,
            @PathVariable("animeId") String animeId
    ) {
        User user = userService.findUserByName(fullName);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        userService.removeFavorite(user, animeService.findAnimeById(animeId));

        UserResponse response = createUserResponse(user);
        response.setFavoriteAnime(user.getFavoriteAnime());

        return ResponseEntity.ok(response);
    }

    private UserResponse createUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setFullName(user.getFullName());
        response.setAge(user.getAge());
        response.setBio(user.getBio());
        response.setFavoriteAnime(user.getFavoriteAnime());
        response.setFriends(user.getFriends());
        response.setDisplayName(user.getDisplayName());
        return response;
    }

}
