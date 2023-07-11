package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.*;
import com.kenzie.appserver.exceptions.NicknameAlreadyExistsException;
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

    LoginController(LoginService loginService) {
        this.loginService = loginService;
    }


    @PostMapping("/createLogin")
    public ResponseEntity<LoginResponse> createLogin(@RequestBody LoginCreateRequest loginCreateRequest) {
        int success = 0;
        try {
            success = loginService.createLogin(loginCreateRequest.getEmail(), loginCreateRequest.getPassword(),
                    loginCreateRequest.getNickname());

        } catch (NicknameAlreadyExistsException nicknameError) {
            return ResponseEntity.status(449).build();
            //449 = nickname already exists
        }
        if (success == 200) {
            //200 login created
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } else {
            return ResponseEntity.status(444).build();
            //444 = email already exists
        }
    }


    @PostMapping("/login")
    public ResponseEntity<LoginGetResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        String success = loginService.login(loginRequest.getEmail());

        if (success != null) {
            LoginGetResponse response = new LoginGetResponse();
            response.setPassword(success);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }





    @PutMapping("/updateEmail")
    public ResponseEntity<LoginResponse> updateEmailByEmail(@RequestBody LoginUpdateLoginRequest loginUpdateLoginRequest) {
        boolean success = loginService.updateEmailByEmail(loginUpdateLoginRequest.getEmail(),
                loginUpdateLoginRequest.getNewEmail());

        return success ? ResponseEntity.status(HttpStatus.ACCEPTED).build() : ResponseEntity.status(HttpStatus.CONFLICT).build();
    }


    @PutMapping("/changePassword")
    public ResponseEntity<LoginResponse> updatePasswordByEmail(@RequestBody LoginUpdatePasswordRequest loginUpdatePasswordRequest) {
        boolean success = loginService.updatePasswordByEmail(loginUpdatePasswordRequest.getEmail(), loginUpdatePasswordRequest.getNewPassword());

        return success ? ResponseEntity.status(HttpStatus.ACCEPTED).build() : ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @DeleteMapping("/deleteAccount")
    public ResponseEntity<LoginResponse> deleteLoginByEmail(@RequestBody LoginDeleteRequest loginDeleteRequest) {
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