package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.*;
import com.kenzie.appserver.service.UserService;
import com.kenzie.appserver.service.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{displayName}/searchByDisplayName")
    public ResponseEntity<UserResponse> searchByDisplayName(@PathVariable("displayName") String displayName) {
        User user = userService.findUserByName(displayName);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        UserResponse response = createUserResponse(user);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/updateUser")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserUpdateRequest request) {
        User user = new User(request.getFollowers(), request.getFollowing(), request.getEmail(), request.getUserId(), request.getFavoriteAnime(), request.getFullName(), request.getDisplayName(), request.getAge(), request.getBio());

        userService.updateUser(user);

        UserResponse response = createUserResponse(user);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/addUser")
    public ResponseEntity<UserResponse> addNewUser(@RequestBody UserCreateRequest request) {
        User user = new User(request.getFollowers(), request.getFollowing(), request.getEmail(), request.getUserId(), request.getFavoriteAnime(), request.getFullName(), request.getDisplayName(), request.getAge(), request.getBio());

        userService.addNewUser(user);

        UserResponse response = createUserResponse(user);

        return ResponseEntity.created(URI.create("/user/" + response.getDisplayName())).body(response);
    }

    @DeleteMapping("/{displayName}/deleteByDisplayName")
    public ResponseEntity deleteUserByDisplayName(@PathVariable("displayName") String displayName) {
        userService.deleteUser(displayName);

        return ResponseEntity.status(204).build();
    }

    @PostMapping("/{displayName}/followUser/{friendDisplayName}")
    public ResponseEntity<UserResponse> followUser(
            @PathVariable("displayName") String displayName,
            @PathVariable("friendDisplayName") String friendDisplayName
    ) {
        User user = userService.findUserByName(displayName);
        User friend = userService.findUserByName(friendDisplayName);

        if (user == null || friend == null) {
            return ResponseEntity.notFound().build();
        }

        List<String> followed = userService.follow(user.getDisplayName(), friend.getDisplayName());

        UserResponse response = createUserResponse(user);
        response.setFollowing(followed);

        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{displayName}/unfollowUser/{friendFullName}")
    public ResponseEntity<UserResponse> unfollowUser(
            @PathVariable("displayName") String displayName,
            @PathVariable("friendFullName") String friendFullName
    ) {
        User user = userService.findUserByName(displayName);
        User friend = userService.findUserByName(friendFullName);

        if (user == null || friend == null) {
            return ResponseEntity.notFound().build();
        }

        userService.unfollow(user.getDisplayName(), friend.getDisplayName());

        UserResponse response = createUserResponse(user);
        response.setFollowers(user.getFollowers());
        response.setFollowing(user.getFollowing());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{displayName}/addFavorite/{animeId}")
    public ResponseEntity<UserResponse> addFavorite(
            @PathVariable("displayName") String displayName,
            @PathVariable("animeId") int animeId
    ) {
        User user = userService.findUserByName(displayName);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }


        UserResponse response = createUserResponse(user);
        response.setFavoriteAnime(userService.addNewFavorite(user.getDisplayName(), String.valueOf(animeId)));

        return ResponseEntity.ok(response);
    }


        @DeleteMapping("/{displayName}/removeFavorite/{animeId}/removeFavorite")
    public ResponseEntity<UserResponse> removeFavorite(
            @PathVariable("displayName") String displayName,
            @PathVariable("animeId") String animeId
    ) {
        User user = userService.findUserByName(displayName);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        userService.removeFavorite(user.getDisplayName(), animeId);

        UserResponse response = createUserResponse(user);
        response.setFavoriteAnime(user.getFavoriteAnime());

        return ResponseEntity.ok(response);
    }

    private UserResponse createUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setUserId(user.getUserId());
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullName());
        response.setAge(user.getAge());
        response.setBio(user.getBio());
        response.setFavoriteAnime(user.getFavoriteAnime());
        response.setFollowers(user.getFollowers());
        response.setDisplayName(user.getDisplayName());
        return response;
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserDisplayNameResponse> findDisplayNameByEmail(@PathVariable String email) {
        String displayName = userService.findDisplayNameByEmail(email);;

        if (displayName != null) {
            UserDisplayNameResponse response = new UserDisplayNameResponse();
            response.setDisplayName(displayName);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}



