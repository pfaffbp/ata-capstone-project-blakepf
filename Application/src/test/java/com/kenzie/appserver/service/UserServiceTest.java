package com.kenzie.appserver.service;

import com.kenzie.appserver.config.CacheUserStore;
import com.kenzie.appserver.repositories.UserRepository;
import com.kenzie.appserver.service.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private CacheUserStore cacheUserStore;

    private UserService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new UserService(repository, cacheUserStore);
    }

    @Test
    void findUser_userExists_returnsTrue() {
        //GIVEN

        //WHEN

        //THEN
    }
}
