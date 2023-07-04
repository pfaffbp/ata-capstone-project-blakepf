package com.kenzie.appserver.service;

import com.kenzie.appserver.exceptions.NicknameAlreadyExistsException;
import com.kenzie.appserver.repositories.LoginRepository;
import com.kenzie.appserver.repositories.UserRepository;
import com.kenzie.appserver.repositories.model.LoginRecord;
import com.kenzie.appserver.repositories.model.UserRecord;
import com.kenzie.appserver.service.model.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class LoginService {

    private final LoginRepository loginRepository;
    private final UserRepository userRepository;


    @Autowired
    public LoginService(LoginRepository loginRepository, UserRepository userRepository ) {

        this.loginRepository = loginRepository;
        this.userRepository = userRepository;
    }

    public int createLogin(String email, String password, String nickname) {
        Optional<LoginRecord> record = loginRepository.findByEmail(email);
        Optional<UserRecord> userRecordCheck = userRepository.findByDisplayName(nickname);

        if (record.isPresent()) {
            return 444;
            // 444 = email already exists
        }else if (userRecordCheck.isPresent()){
            throw new NicknameAlreadyExistsException(nickname);

        } else {
            String userId = UUID.randomUUID().toString();

            LoginRecord loginRecord = new LoginRecord();
            loginRecord.setUserId(userId);
            loginRecord.setEmail(email);
            loginRecord.setPassword(password);
            loginRepository.save(loginRecord);

            UserRecord userRecord = new UserRecord();
            userRecord.setEmail(email);
            userRecord.setUserId(userId);
            userRecord.setDisplayName(nickname);
            userRecord.setFullName(" ");
            userRecord.setBio(" ");
            userRepository.save(userRecord);
            return 200;
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