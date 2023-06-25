package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.UserCreateRequest;
import com.kenzie.appserver.controller.model.UserResponse;
import com.kenzie.appserver.controller.model.UserUpdateRequest;
import com.kenzie.appserver.service.UserService;
import com.kenzie.appserver.service.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static java.util.UUID.randomUUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    private UserResponse createUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setUserId((user.getUserId()));
        userResponse.setName(user.getName());
        userResponse.setEmail(user.getEmail());
        userResponse.setUsername(user.getUsername());
        userResponse.setBirthday(user.getBirthday());
        return userResponse;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> searchByUserId(@PathVariable("userId") String userId) {
        User user = userService.findUserById(userId);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        UserResponse userResponse = createUserResponse(user);

        return ResponseEntity.ok(userResponse);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> users = userService.findAllUsers();

        if (users == null || users.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        List<UserResponse> response = new ArrayList<>();
        for (User user : users) {
            response.add(this.createUserResponse(user));
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<UserResponse> addNewUser(@RequestBody UserCreateRequest userCreateRequest) {
        User user = new User(randomUUID().toString(),
                userCreateRequest.getName(),
                userCreateRequest.getEmail(),
                userCreateRequest.getUsername(),
                userCreateRequest.getBirthday()
        );
        userService.addNewUser(user);

        UserResponse userResponse = createUserResponse(user);

        return ResponseEntity.created(URI.create("/users/" + userResponse.getUserId())).body(userResponse);
    }

    @PutMapping
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        User user = new User((userUpdateRequest.getUserId()),
                userUpdateRequest.getName(),
                userUpdateRequest.getEmail(),
                userUpdateRequest.getUsername(),
                userUpdateRequest.getBirthday());

        userService.updateUser(user);

        UserResponse userResponse = createUserResponse(user);

        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity deleteUserById(@PathVariable("userId") String userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.status(204).build();
    }


}


