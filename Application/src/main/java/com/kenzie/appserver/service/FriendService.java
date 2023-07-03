package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.FriendRepository;
import com.kenzie.appserver.repositories.UserRepository;
import com.kenzie.appserver.service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendService {
    @Autowired
    FriendRepository friendRepository;
    @Autowired
    UserRepository userRepository;

    public void addFriend(User user) throws NullPointerException {

    }
}
