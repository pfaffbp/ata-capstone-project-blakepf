package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.*;
import com.kenzie.appserver.service.CatalogService;
import com.kenzie.appserver.service.UserService;
import com.kenzie.appserver.service.model.Anime;
import com.kenzie.appserver.service.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> userList = userService.findAllUsers();
        if (userList == null || userList.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        List<UserResponse> response = new ArrayList<>();

        for (User users : userList) {
            response.add(this.createUserResponse(users));
        }

        return ResponseEntity.ok(response);
    }

    @PutMapping("/updateUser")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserUpdateRequest request) {
        User user = new User(request.getUserId(), request.getEmail(), request.getFullName(), request.getAge(),
                request.getFullName(), request.getBio());

        userService.updateUser(user);

        UserResponse response = createUserResponse(user);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/addUser")
    public ResponseEntity<UserResponse> addNewUser(@RequestBody UserCreateRequest request) {
        User user = new User(request.getUserId(), request.getEmail(), request.getFullName(),
                request.getAge(), request.getDisplayName(), request.getBio());

        userService.addNewUser(user);

        UserResponse response = createUserResponse(user);

        return ResponseEntity.created(URI.create("/user/" + response.getDisplayName())).body(response);
    }

    @DeleteMapping("/{displayName}/deleteByDisplayName")
    public ResponseEntity<UserResponse> deleteUserByDisplayName(@PathVariable("displayName") String displayName) {
        userService.deleteUser(displayName);

        return ResponseEntity.status(204).build();
    }

    @PostMapping("/{displayName}/addFriend/{friendFullName}")
    public ResponseEntity<UserResponse> addFriend(
            @PathVariable("displayName") String displayName,
            @PathVariable("friendFullName") String friendDisplayName
    ) {
        User user = userService.findUserByName(displayName);
        User friend = userService.findUserByName(friendDisplayName);

        if (user == null || friend == null) {
            return ResponseEntity.notFound().build();
        }

        List<String> friends = userService.addFriend(user.getDisplayName(), friend.getDisplayName());

        UserResponse response = createUserResponse(user);
        response.setFriends(friends);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{displayName}/removeFriend/{friendFullName}")
    public ResponseEntity<UserResponse> removeFriend(
            @PathVariable("displayName") String displayName,
            @PathVariable("friendFullName") String friendFullName
    ) {
        User user = userService.findUserByName(displayName);
        User friend = userService.findUserByName(friendFullName);

        if (user == null || friend == null) {
            return ResponseEntity.notFound().build();
        }

        userService.removeFriend(user.getDisplayName(), friend.getDisplayName());

        UserResponse response = createUserResponse(user);
        response.setFriends(user.getFriends());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{displayName}/addFavorite/addFavorite")
    public ResponseEntity<UserResponse> addFavorite(
            @PathVariable("displayName") String displayName,
            @RequestBody FavoriteAnimeRequest favoriteAnimeRequest
    ) {
        User user = userService.findUserByName(displayName);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        if (user.getFavoriteAnime().size() < 10) {
            List<String> favoriteAnime = userService.addNewFavorite(user.getDisplayName(),
                    favoriteAnimeRequest.toString());

            UserResponse response = createUserResponse(user);
            response.setFavoriteAnime(favoriteAnime);

            return ResponseEntity.ok(response);
        } else {
            //not sure how else to let the user know that they are at their limit, will come back to fix this
            return ResponseEntity.badRequest().build();
        }
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
        response.setFriends(user.getFriends());
        response.setDisplayName(user.getDisplayName());
        return response;
    }

}



