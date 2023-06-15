package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.LoginCreateRequest;
import com.kenzie.appserver.controller.model.LoginRequest;
import com.kenzie.appserver.service.LoginService;
import com.kenzie.appserver.service.model.Login;
import org.apache.tomcat.jni.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/Login")
public class LoginController {

    private LoginService loginService;

    LoginController(LoginService loginService){
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> createLogin(@RequestBody LoginCreateRequest request){
        boolean accepted = loginService.createLogin(request.getEmail(), request.getPassword());
        return accepted ? ResponseEntity.status(HttpStatus.ACCEPTED).build() : ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody @Valid LoginRequest request) {
        Login login = loginService.login(request.getEmail(), request.getPassword());
        return login != null ? ResponseEntity.status(HttpStatus.ACCEPTED).build(): ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @DeleteMapping("/deleteLoginByEmail")
    public ResponseEntity<Void> deleteByEmail(@RequestParam String email) {
        boolean success = loginService.deleteLoginByEmail(email);
        return success ? ResponseEntity.status(HttpStatus.ACCEPTED).build() : ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<Void> updatePasswordByEmail(@RequestParam String email, @RequestParam String password) {
        boolean success = loginService.updatePasswordByEmail(email, password);
        return success ? ResponseEntity.status(HttpStatus.ACCEPTED).build()  : ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @PutMapping("/updateEmail")
    public ResponseEntity<Void> updateEmailByEmail(@RequestParam String email, @RequestParam String password) {
        boolean success = loginService.updateEmailByEmail(email, password);
        return success ? ResponseEntity.status(HttpStatus.ACCEPTED).build()  : ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
