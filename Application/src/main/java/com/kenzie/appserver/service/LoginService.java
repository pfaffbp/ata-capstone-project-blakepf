package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.LoginRepository;
import com.kenzie.appserver.repositories.model.LoginRecord;
import com.kenzie.appserver.service.model.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class LoginService {

    private final LoginRepository loginRepository;

    @Autowired
    public LoginService(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    public boolean createLogin(String email, String password) {
        Optional<LoginRecord> record = loginRepository.findByEmail(email);
        if (record.isPresent()) {
            return false;
        } else {
            String userId = UUID.randomUUID().toString();

            LoginRecord loginRecord = new LoginRecord();
            loginRecord.setUserId(userId);
            loginRecord.setEmail(email);
            loginRecord.setPassword(password);
            loginRepository.save(loginRecord);

        /*    UserRecros userRecord = new UserRecord;
            userRecord.setEmail(email);
            userRecord.setUserid(userId);
            user*/
            return true;
        }

    }

    public String login(String email) {
        Optional<LoginRecord> record = loginRepository.findByEmail(email);
        if (record.isPresent()) {
            return record.get().getPassword();
        } else {
            return null;
        }
    }


    public boolean deleteLoginByEmail(String email, String password) {
        Optional<LoginRecord> record = loginRepository.findByEmail(email);
        if (record.isPresent() && record.get().getPassword().equals(password)){
            loginRepository.delete(record.get());
            return true;
        } else {
            return false;
        }
    }

    public boolean updatePasswordByEmail(String email, String updatePassword) {
        Optional<LoginRecord> record = loginRepository.findByEmail(email);
        if (record.isPresent()) {
            LoginRecord loginRecord = record.get();
            loginRecord.setPassword(updatePassword);
            loginRepository.save(loginRecord);
            return true;
        } else {
            return false;
        }
    }

    public boolean updateEmailByEmail(String email, String updatedEmail) {
        Optional<LoginRecord> record = loginRepository.findByEmail(email);
        if (record.isPresent()) {
            LoginRecord loginRecord = record.get();
            loginRecord.setEmail(updatedEmail);
            loginRepository.save(loginRecord);
            return true;
        } else {
            return false;
        }
    }

    public String getUserIdByEmail(String email) {
        Optional<LoginRecord> record = loginRepository.findByEmail(email);
        if (record.isPresent()) {
            return record.get().getUserId();
        } else {
            return null;
        }
    }


}