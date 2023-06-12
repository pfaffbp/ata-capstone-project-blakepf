package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.UserResponse;
import com.kenzie.appserver.controller.model.UserUpdateRequest;
import com.kenzie.appserver.service.UserService;
import com.kenzie.appserver.service.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

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
        User user = new User(request.getFullName(), request.getAge(), request.getBio());

        userService.updateUser(user);

        UserResponse response = createUserResponse(user);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{fullName}")
    public ResponseEntity deleteUserByFullName(@PathVariable("fullName") String fullName) {
        userService.deleteUser(fullName);

        return ResponseEntity.status(204).build();
    }

    private UserResponse createUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setFullName(user.getFullName());
        response.setAge(user.getAge());
        response.setBio(user.getBio());
        response.setFavoriteAnime(user.getFavoriteAnime());
        response.setFriends(user.getFriends());
        return response;
    }

}
