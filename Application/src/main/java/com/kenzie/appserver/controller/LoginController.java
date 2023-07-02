package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.*;
import com.kenzie.appserver.service.LoginService;
import com.kenzie.appserver.service.model.Login;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/login")
public class LoginController {

    private LoginService loginService;

    LoginController(LoginService loginService){
        this.loginService = loginService;
    }


    @PostMapping("/createLogin")
    public ResponseEntity<LoginResponse> createLogin(@RequestBody LoginCreateRequest loginCreateRequest){
        boolean success = loginService.createLogin(loginCreateRequest.getEmail(), loginCreateRequest.getPassword());

        return success ? ResponseEntity.status(HttpStatus.ACCEPTED).build() : ResponseEntity.status(HttpStatus.CONFLICT).build();
    }


    @PostMapping("/login")
    public ResponseEntity<Login> login(@RequestBody @Valid LoginRequest loginRequest){
        Login login =  loginService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return login != null ? ResponseEntity.ok(login) : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


    @PutMapping("/updateEmail")
    public ResponseEntity<LoginResponse> updateEmailByEmail (@RequestBody LoginUpdateLoginRequest loginUpdateLoginRequest){
        boolean success = loginService.updateEmailByEmail(loginUpdateLoginRequest.getEmail(),
                loginUpdateLoginRequest.getNewEmail(), loginUpdateLoginRequest.getPassword());

        return success ? ResponseEntity.status(HttpStatus.ACCEPTED).build() : ResponseEntity.status(HttpStatus.CONFLICT).build();
    }


    @PutMapping("/changePassword")
    public ResponseEntity<LoginResponse> updatePasswordByEmail(@RequestBody LoginUpdatePasswordRequest loginUpdatePasswordRequest){
        boolean success = loginService.updatePasswordByEmail(loginUpdatePasswordRequest.getEmail(),
                loginUpdatePasswordRequest.getPassword(), loginUpdatePasswordRequest.getNewPassword());

        return success ? ResponseEntity.status(HttpStatus.ACCEPTED).build() : ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @DeleteMapping("/deleteAccount")
    public ResponseEntity<LoginResponse> deleteLoginByEmail(@RequestBody LoginDeleteRequest loginDeleteRequest){
        boolean success = loginService.deleteLoginByEmail(loginDeleteRequest.getEmail(), loginDeleteRequest.getPassword());

        return success ? ResponseEntity.status(HttpStatus.ACCEPTED).build() : ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserIdResponse> getUserIdByEmail(@PathVariable String email) {
        String userId = loginService.getUserIdByEmail(email);

        if (userId != null) {
            UserIdResponse response = new UserIdResponse();
            response.setUserId(userId);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
