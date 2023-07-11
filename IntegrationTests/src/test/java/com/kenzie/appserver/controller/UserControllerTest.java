package com.kenzie.appserver.controller;

import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.UserCreateRequest;
import com.kenzie.appserver.controller.model.UserUpdateRequest;
import com.kenzie.appserver.service.CatalogService;
import com.kenzie.appserver.service.UserService;
import com.kenzie.appserver.service.model.Anime;
import com.kenzie.appserver.service.model.User;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;

@IntegrationTest
public class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserService userService;
    @Autowired
    private CatalogService animeService;

    private final MockNeat mockNeat = MockNeat.threadLocal();

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testSearchByDisplayName() throws Exception {
        // Create a user using the userService
        String userId = UUID.randomUUID().toString();
        User user = new User(new ArrayList<>(), new ArrayList<>(), "email", userId,
                new ArrayList<>(), "fullName", "testUser", 27, "bio");
        userService.addNewUser(user);

        mvc.perform(MockMvcRequestBuilders.get("/user/{displayName}/searchByDisplayName", "testUser"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.displayName").value("testUser"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("email"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName").value("fullName"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(27))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(userId));
    }

    @Test
    public void testUpdateUser() throws Exception {
        // Create a user using the userService
        User user = new User(new ArrayList<>(), new ArrayList<>(), "email", UUID.randomUUID().toString(), new ArrayList<>(),
                "fullName", "displayName", 28, "bio");
        userService.addNewUser(user);

        UserUpdateRequest request = new UserUpdateRequest();
        request.setUserId(user.getUserId());
        request.setFollowers(user.getFollowers());
        request.setFollowing(user.getFollowing());
        request.setEmail("updated@example.com");
        request.setFavoriteAnime(user.getFavoriteAnime());
        request.setFullName("UpdatedUser");
        request.setDisplayName("testuser");
        request.setAge(30);
        request.setBio("Updated bio");

        mvc.perform(MockMvcRequestBuilders.put("/user/updateUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(user.getUserId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("updated@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName").value("UpdatedUser"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(30))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bio").value("Updated bio"));
    }

    @Test
    public void testAddNewUser() throws Exception {
        UserCreateRequest request = new UserCreateRequest();
        request.setFollowers(new ArrayList<>());
        request.setFollowing(new ArrayList<>());
        request.setEmail("newuser@example.com");
        request.setUserId(UUID.randomUUID().toString());
        request.setFavoriteAnime(new ArrayList<>());
        request.setFullName("NewUser");
        request.setDisplayName("newuser");
        request.setAge(25);
        request.setBio("New bio");

        mvc.perform(MockMvcRequestBuilders.post("/user/addUser")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/user/newuser"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("newuser@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName").value("NewUser"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(25))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bio").value("New bio"));
    }

/*    @Test
    public void testDeleteUserByDisplayName() throws Exception {
        // Create a user using the userService
        User user = new User(new ArrayList<>(), new ArrayList<>(), "email", UUID.randomUUID().toString(), new ArrayList<>(),
                "fullName", "testuser", 28, "bio");
        userService.addNewUser(user);

        mvc.perform(MockMvcRequestBuilders.delete("/user/{displayName}/deleteByDisplayName", "testuser")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }*/

//    @Test
//    public void testFollowUser() throws Exception {
//        // Create two users using the userService
//        User user1 = new User(new ArrayList<>(), new ArrayList<>(), "email", UUID.randomUUID().toString(),
//                new ArrayList<>(), "fullName", "displayName", 27, "bio");
//        userService.addNewUser(user1);
//
//        User user2 = new User(new ArrayList<>(), new ArrayList<>(), "email2", UUID.randomUUID().toString(),
//                new ArrayList<>(), "fullName2", "displayName2", 27, "bio2");
//        userService.addNewUser(user2);
//
//        mvc.perform(MockMvcRequestBuilders.post("/user/{displayName}/followUser/{friendFullName}", "displayName", "displayName2")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }

    @Test
    public void testUnfollowUser() throws Exception {
        // Create two users using the userService
        User user1 = new User(new ArrayList<>(), new ArrayList<>(), "email", UUID.randomUUID().toString(),
                new ArrayList<>(), "fullName", "displayName", 27, "bio");
        userService.addNewUser(user1);

        User user2 = new User(new ArrayList<>(), new ArrayList<>(), "email2", UUID.randomUUID().toString(),
                new ArrayList<>(), "fullName2", "displayName2", 27, "bio2");
        userService.addNewUser(user2);

        userService.follow("displayName", "displayName2");

        mvc.perform(MockMvcRequestBuilders.delete("/user/{displayName}/unfollowUser/{friendFullName}", "displayName", "displayName2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.displayName").value("displayName"));
    }

//    @Test
//    public void testAddFavorite() throws Exception {
//        // Create a user using the userService
//        User user = new User(new ArrayList<>(), new ArrayList<>(), "email", UUID.randomUUID().toString(), new ArrayList<>(),
//                "fullName", "testuser", 28, "bio");
//
//        userService.addNewUser(user);
//
//        Anime anime = new Anime("title", "animeId", "description", "image",
//                01, "season", 1, 2, 3, new ArrayList<>());
//
//        animeService.addNewAnime(anime);
//
//
//        mvc.perform(MockMvcRequestBuilders.post("/user/{displayName}/addFavorite/{animeId}", "testuser", "animeId")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.displayName").value("testuser"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(user.getUserId()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(28))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("email"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName").value("fullName"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.bio").value("bio"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.favoriteAnime[0]").value("animeId"));
//    }

//    @Test
//    public void testRemoveFavorite() throws Exception {
//        // Create a user using the userService
//        Anime anime = new Anime("title", "animeId", "description", "image",
//                01, "season", 1, 2, 3, new ArrayList<>());
//        animeService.addNewAnime(anime);
//
//        User user = new User(new ArrayList<>(), new ArrayList<>(), "email", UUID.randomUUID().toString(), new ArrayList<>(),
//                "fullName", "testuser", 28, "bio");
//        user.getFavoriteAnime().add("animeId");
//
//        userService.addNewUser(user);
//
//        mvc.perform(MockMvcRequestBuilders.delete("/user/{displayName}/removeFavorite/{animeId}/removeFavorite", "testuser", "123")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.displayName").value("testuser"));
//    }

    @Test
    public void testFindDisplayNameByEmail() throws Exception {
        // Create a user using the userService
        User user = new User(new ArrayList<>(), new ArrayList<>(), "testuser@example.com", UUID.randomUUID().toString(), new ArrayList<>(),
                "fullName", "testuser", 28, "bio");
        userService.addNewUser(user);

        mvc.perform(MockMvcRequestBuilders.get("/user/{email}", "testuser@example.com"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.displayName").value("testuser"));
    }

    // Helper method to convert an object to JSON string
    private String asJsonString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
