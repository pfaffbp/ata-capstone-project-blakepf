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
            LoginRecord loginRecord = new LoginRecord();
            loginRecord.setUserId(UUID.randomUUID().toString());
            loginRecord.setEmail(email);
            loginRecord.setPassword(password);
            loginRepository.save(loginRecord);
            return true;
        }

    }

/*    public Login login(String email, String password) {
        Optional<LoginRecord> record = loginRepository.findByEmail(email);
        if (record.isPresent()) {
            LoginRecord loginRecord = record.get();
            if (loginRecord.getPassword().equals(password)) {
                return new Login(email, password);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }*/

    public Login login(String email, String password) {
        Optional<LoginRecord> record = loginRepository.findByEmail(email);
        if (record.isPresent()) {
            LoginRecord loginRecord = record.get();
            String storedPW = loginRecord.getPassword();
            String enteredPassword = password;
            if (loginRecord.getPassword().equals(password)) {
                return new Login(email, password);
            } else {
                return null;
            }
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

    public boolean updatePasswordByEmail(String email, String password, String updatePassword) {
        Optional<LoginRecord> record = loginRepository.findByEmail(email);
        if (record.isPresent() && record.get().getPassword().equals(password)) {
            LoginRecord loginRecord = record.get();
            loginRecord.setPassword(updatePassword);
            loginRepository.save(loginRecord);
            return true;
        } else {
            return false;
        }
    }

    public boolean updateEmailByEmail(String email, String updatedEmail, String password) {
        Optional<LoginRecord> record = loginRepository.findByEmail(email);
        if (record.isPresent() && record.get().getPassword().equals(password)) {
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
