package com.kenzie.appserver.service;

import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.config.CacheUserStore;
import com.kenzie.appserver.service.model.User;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
@IntegrationTest
public class CacheManagerUserTest {

    @Autowired
    CacheManager cacheManager;

    @Autowired
    UserService userService;

    @Autowired
    private CacheUserStore Cache;

    private final MockNeat mockNeat = MockNeat.threadLocal();

    @Test
    public void userCache_InsertIntoCache() throws Exception {
        List<String> followers = new ArrayList<>();
        List<String> following = new ArrayList<>();
        String email = mockNeat.strings().valStr();
        String userId = mockNeat.strings().valStr();
        List<String> favoriteAnime = new ArrayList<>();
        String fullName = mockNeat.strings().valStr();
        String displayName = mockNeat.strings().valStr();
        int age = 30;
        String bio = mockNeat.strings().valStr();

        User user = new User(followers, following, email, userId, favoriteAnime, fullName,
                displayName, age, bio);
        userService.addNewUser(user);
        userService.findUserByName(displayName);

        User userFromCache = Cache.get(user.getDisplayName());

        assertThat(userFromCache).isNotNull();
        assertThat(userFromCache.getDisplayName()).isEqualTo(displayName);
    }
}

