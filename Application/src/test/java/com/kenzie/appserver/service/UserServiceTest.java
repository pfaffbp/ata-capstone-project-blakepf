package com.kenzie.appserver.service;

import com.kenzie.appserver.config.CacheUserStore;
import com.kenzie.appserver.repositories.CatalogRepository;
import com.kenzie.appserver.repositories.UserRepository;
import com.kenzie.appserver.repositories.model.CatalogRecord;
import com.kenzie.appserver.repositories.model.UserRecord;
import com.kenzie.appserver.service.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private CacheUserStore cacheUserStore;

    @Mock
    private CatalogRepository animeRepository;

    @InjectMocks
    private UserService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new UserService(repository, cacheUserStore, animeRepository);
    }

    @Test
    void findUser_userExists_returnsTrue() {
        //GIVEN
        String userId = randomUUID().toString();
        String email = "randomemail@gmail.com";
        String fullName = "John Doe";
        int age = 27;
        String displayName = "AnimeLover96";
        String bio = "I am a huge lover of all things Anime";
        User user = new User(new ArrayList<>(), new ArrayList<>(), email, userId, new ArrayList<>(), fullName, displayName, age, bio);

        UserRecord userRecord = new UserRecord();
        userRecord.setUserId(user.getUserId());
        userRecord.setEmail(user.getEmail());
        userRecord.setFullName(user.getFullName());
        userRecord.setAge(user.getAge());
        userRecord.setDisplayName(user.getDisplayName());
        userRecord.setBio(user.getBio());
        repository.save(userRecord);

        //WHEN
        when(repository.findByDisplayName(displayName)).thenReturn(Optional.of(userRecord));
        User foundUser = service.findUserByName(displayName);


        //THEN
        assertNotNull(foundUser, "The user was returned");
        assertEquals(userRecord.getUserId(), foundUser.getUserId(), "The userId matches");
        assertEquals(userRecord.getDisplayName(), foundUser.getDisplayName(), "The display names match");
        assertEquals(userRecord.getEmail(), foundUser.getEmail(), "The emails match");
    }

    @Test
    void findByDisplayName_invalidDisplayName_returnsNull() {
        UserRecord nullRecord = new UserRecord();
        nullRecord.setUserId(null);
        nullRecord.setDisplayName(null);

        when(repository.findByDisplayName(nullRecord.getDisplayName())).thenReturn(Optional.empty());
        User nullUser = service.findUserByName(nullRecord.getDisplayName());

        Assertions.assertNull(nullUser);
    }

    @Test
    void findAllUsers_returnsUsersList() {
        String userId = randomUUID().toString();
        String userId2 = randomUUID().toString();

        UserRecord userRecord = new UserRecord();
        userRecord.setUserId(userId);
        userRecord.setEmail("user1email@gmail.com");
        userRecord.setFullName("Jane Doe");
        userRecord.setAge(27);
        userRecord.setDisplayName("Jane");
        userRecord.setBio("bio of Jane");
        repository.save(userRecord);

        UserRecord userRecord2 = new UserRecord();
        userRecord2.setUserId(userId2);
        userRecord2.setEmail("user2email@gmail.com");
        userRecord2.setFullName("John Doe");
        userRecord2.setAge(27);
        userRecord2.setDisplayName("John");
        userRecord2.setBio("bio of John");
        repository.save(userRecord2);

        List<UserRecord> userRecords = new ArrayList<>();
        userRecords.add(userRecord);
        userRecords.add(userRecord2);

        //WHEN
        when(repository.findAll()).thenReturn(userRecords);
        List<User> users = service.findAllUsers();

        //THEN
        assertNotNull(users, "The users are returned");
        assertEquals(userRecords.size(), users.size(), "The sizes match");
    }

    @Test
    void updateUser_validUser_UpdatesUser(){
        String userId = randomUUID().toString();
        User user = new User(new ArrayList<>(), new ArrayList<>(), "email", userId, new ArrayList<>(),
                "fullName", "displayName", 27, "bio");

        UserRecord userRecord = new UserRecord();
        userRecord.setUserId(user.getUserId());
        userRecord.setFollowers(user.getFollowers());
        userRecord.setFollowing(user.getFollowing());
        userRecord.setFavoriteAnime(user.getFavoriteAnime());
        userRecord.setEmail(user.getEmail());
        userRecord.setFullName(user.getFullName());
        userRecord.setAge(user.getAge());
        userRecord.setDisplayName(user.getDisplayName());
        userRecord.setBio(user.getBio());
        repository.save(userRecord);

        User updated = new User(new ArrayList<>(), new ArrayList<>(), "updatedEmail", userId, new ArrayList<>(), "newFullName",
                "newName", 30, "newBio");

        when(repository.existsById(user.getUserId())).thenReturn(true);

        ArgumentCaptor<UserRecord> argumentCaptor = ArgumentCaptor.forClass(UserRecord.class);
                when(repository.findById(user.getUserId())).thenReturn(Optional.of(userRecord));
                when(repository.save(argumentCaptor.capture())).thenReturn(userRecord);
                when(cacheUserStore.get(user.getUserId())).thenReturn(user);

                service.updateUser(updated);

                verify(repository, times(2)).save(argumentCaptor.capture());
                verify(repository).findById(user.getUserId());
                verify(cacheUserStore).evict(updated.getFullName());

                UserRecord capturedUserRecord = argumentCaptor.getValue();
                assertEquals(capturedUserRecord.getFullName(), userRecord.getFullName());
                assertEquals(capturedUserRecord.getBio(), userRecord.getBio());
                assertEquals(capturedUserRecord.getUserId(), userRecord.getUserId());
                assertEquals(capturedUserRecord.getFavoriteAnime(), userRecord.getFavoriteAnime());
                assertEquals(capturedUserRecord.getFollowers(), userRecord.getFollowers());
                assertEquals(capturedUserRecord.getFollowing(), userRecord.getFollowing());
                assertEquals(capturedUserRecord.getAge(), userRecord.getAge());
                assertEquals(capturedUserRecord.getBio(), userRecord.getBio());
                assertEquals(capturedUserRecord.getDisplayName(), updated.getDisplayName(), "display name was updated properly");
                assertEquals(capturedUserRecord.getEmail(), updated.getEmail(), "email was updated properly");
                assertEquals(capturedUserRecord.getAge(), updated.getAge(), "age was updated properly");
                assertNotEquals(capturedUserRecord.getAge(), user.getAge(), "saved age is different from initial age");
                assertNotEquals(capturedUserRecord.getEmail(), user.getEmail(), "saved email is different from initial email");
                assertNotEquals(capturedUserRecord.getDisplayName(), user.getDisplayName(), "saved display name is different from initial display name");
                assertNotEquals(capturedUserRecord.getBio(), user.getBio(), "saved bio is different from initial bio");
                assertNotEquals(capturedUserRecord.getFullName(), user.getFullName(), "saved fullName is different from initial fullName");
    }
    @Test
    void addNewUserTest() {
        String userId = randomUUID().toString();

        User user = new User(new ArrayList<>(), new ArrayList<>(), "email", userId, new ArrayList<>(),
                "fullName", "displayName", 27, "bio");

        ArgumentCaptor<UserRecord> userRecordArgumentCaptor = ArgumentCaptor.forClass(UserRecord.class);

        User returnedUser = service.addNewUser(user);

        Assertions.assertNotNull(returnedUser);
        verify(repository).save(userRecordArgumentCaptor.capture());

        UserRecord record = userRecordArgumentCaptor.getValue();

        Assertions.assertNotNull(record, "The user record is returned");
        assertEquals(record.getUserId(), user.getUserId(), "The ids match");
        assertEquals(record.getBio(), user.getBio(), "The bios match");
        assertEquals(record.getAge(), user.getAge(), "The ages match");
        assertEquals(record.getEmail(), user.getEmail(), "The emails match");
        assertEquals(record.getDisplayName(), user.getDisplayName(), "The display names match");
        assertEquals(record.getFullName(), user.getFullName(), "The full names match");
    }
    @Test
    void deleteUserTest() {
        String userId = randomUUID().toString();

        UserRecord record = new UserRecord();
        record.setUserId(userId);

        when(repository.existsById(anyString())).thenReturn(true);
        when(repository.findById(anyString())).thenReturn(Optional.of(record));

        when(cacheUserStore.get(record.getUserId())).thenReturn(new User(new ArrayList<>(), new ArrayList<>(), "email",
                userId, new ArrayList<>(), "fullName", "displayName", 27, "bio"));

        service.deleteUser(userId);

        verify(cacheUserStore).evict(record.getUserId());

        verify(repository, times(1)).deleteById(userId);
    }

    @Test
    void addNewFavorite_validUser_addsToList() {
        String userId = randomUUID().toString();
        List<String> favoriteAnime = new ArrayList<>();
        favoriteAnime.add(randomUUID().toString());

        User user = new User(new ArrayList<>(), new ArrayList<>(), "email", userId, favoriteAnime,
                "fullName", "displayName", 27, "bio");

        UserRecord userRecord = new UserRecord();
        userRecord.setUserId(user.getUserId());
        userRecord.setFollowers(user.getFollowers());
        userRecord.setFollowing(user.getFollowing());
        userRecord.setFavoriteAnime(user.getFavoriteAnime());
        userRecord.setEmail(user.getEmail());
        userRecord.setFullName(user.getFullName());
        userRecord.setAge(user.getAge());
        userRecord.setDisplayName(user.getDisplayName());
        userRecord.setBio(user.getBio());
        repository.save(userRecord);

        String animeId = randomUUID().toString();
        CatalogRecord anime = new CatalogRecord();
        anime.setAnimeId(animeId);
        anime.setDescription("description");
        anime.setEpisodes(5);
        anime.setGenre(new ArrayList<>());
        anime.setImage("image");
        anime.setPopularity(10);
        anime.setRating(8);
        anime.setSeason("season");
        anime.setStartDate(0605);
        animeRepository.save(anime);

        when(repository.findByDisplayName(userRecord.getDisplayName())).thenReturn(Optional.of(userRecord));
        when(animeRepository.findById(anime.getAnimeId())).thenReturn(Optional.of(anime));

        List<String> updatedList = service.addNewFavorite(userRecord.getDisplayName(), anime.getAnimeId());

        verify(repository, times(1)).findByDisplayName(userRecord.getDisplayName());
        verify(animeRepository, times(1)).findById(anime.getAnimeId());

        verify(repository, times(2)).save(userRecord);

        assertEquals(userRecord.getFavoriteAnime(), updatedList, "favorite anime list has been updated properly");
        assertEquals(2, updatedList.size(), "favorite anime list size is correct");
    }

    @Test
    void addNewFavorite_animeNotInFavorites_throwsException() {
        String animeId = randomUUID().toString();
        CatalogRecord anime = new CatalogRecord();
        anime.setAnimeId(animeId);
        anime.setDescription("description");
        anime.setEpisodes(5);
        anime.setGenre(new ArrayList<>());
        anime.setImage("image");
        anime.setPopularity(10);
        anime.setRating(8);
        anime.setSeason("season");
        anime.setStartDate(0605);
        animeRepository.save(anime);

        String userId = randomUUID().toString();
        List<String> favorites = new ArrayList<>();
        favorites.add(animeId);

        UserRecord record = new UserRecord();

        record.setUserId(userId);
        record.setFavoriteAnime(favorites);
        repository.save(record);

        when(repository.findByDisplayName(record.getDisplayName())).thenReturn(Optional.of(record));
        when(animeRepository.findById(anime.getAnimeId())).thenReturn(Optional.of(anime));

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            service.addNewFavorite(record.getDisplayName(), anime.getAnimeId());
        });
        verify(repository, times(1)).findByDisplayName(record.getDisplayName());
        verify(animeRepository, times(1)).findById(anime.getAnimeId());
    }
    @Test
    void removeFavoriteTest() {
        String animeId = randomUUID().toString();
        CatalogRecord anime = new CatalogRecord();
        anime.setAnimeId(animeId);
        anime.setDescription("description");
        anime.setEpisodes(5);
        anime.setGenre(new ArrayList<>());
        anime.setImage("image");
        anime.setPopularity(10);
        anime.setRating(8);
        anime.setSeason("season");
        anime.setStartDate(0605);
        animeRepository.save(anime);

        String userId = randomUUID().toString();
        List<String> favorites = new ArrayList<>();
        favorites.add(animeId);
        favorites.add("animeToKeep");

        User user = new User(new ArrayList<>(), new ArrayList<>(), "email", userId, favorites,
                        "fullName", "displayName", 27, "bio");

        UserRecord userRecord = new UserRecord();
        userRecord.setUserId(user.getUserId());
        userRecord.setFollowers(user.getFollowers());
        userRecord.setFollowing(user.getFollowing());
        userRecord.setFavoriteAnime(user.getFavoriteAnime());
        userRecord.setEmail(user.getEmail());
        userRecord.setFullName(user.getFullName());
        userRecord.setAge(user.getAge());
        userRecord.setDisplayName(user.getDisplayName());
        userRecord.setBio(user.getBio());
        repository.save(userRecord);

        when(repository.findByDisplayName(userRecord.getDisplayName())).thenReturn(Optional.of(userRecord));
        when(animeRepository.findById(anime.getAnimeId())).thenReturn(Optional.of(anime));

        List<String> updated = service.removeFavorite(userRecord.getDisplayName(), animeId);

        verify(repository, times(1)).findByDisplayName(userRecord.getDisplayName());
        verify(animeRepository, times(1)).findById(anime.getAnimeId());

        verify(repository, times(2)).save(userRecord);

        assertEquals(userRecord.getFavoriteAnime(), updated, "favorite anime list has been updated properly");
        assertEquals(1, updated.size(), "favorite anime list size is correct");
        assertEquals("animeToKeep", updated.get(0), "correct anime was kept");
    }
    @Test
    void removeFavorite_existingAnime_throwsException() {
        String animeId = randomUUID().toString();
        CatalogRecord anime = new CatalogRecord();
        anime.setAnimeId(animeId);
        anime.setDescription("description");
        anime.setEpisodes(5);
        anime.setGenre(new ArrayList<>());
        anime.setImage("image");
        anime.setPopularity(10);
        anime.setRating(8);
        anime.setSeason("season");
        anime.setStartDate(0605);
        animeRepository.save(anime);

        String userId = randomUUID().toString();
        List<String> favorites = new ArrayList<>();
        favorites.add("animeToKeep");

        User user = new User(new ArrayList<>(), new ArrayList<>(), "email", userId, favorites,
                "fullName", "displayName", 27, "bio");

        UserRecord userRecord = new UserRecord();
        userRecord.setUserId(user.getUserId());
        userRecord.setFollowers(user.getFollowers());
        userRecord.setFollowing(user.getFollowing());
        userRecord.setFavoriteAnime(user.getFavoriteAnime());
        userRecord.setEmail(user.getEmail());
        userRecord.setFullName(user.getFullName());
        userRecord.setAge(user.getAge());
        userRecord.setDisplayName(user.getDisplayName());
        userRecord.setBio(user.getBio());
        repository.save(userRecord);

        when(repository.findByDisplayName(userRecord.getDisplayName())).thenReturn(Optional.of(userRecord));
        when(animeRepository.findById(anime.getAnimeId())).thenReturn(Optional.of(anime));

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            service.removeFavorite(userRecord.getDisplayName(), anime.getAnimeId());
        });

        verify(repository, times(1)).findByDisplayName(userRecord.getDisplayName());
        verify(animeRepository, times(1)).findById(anime.getAnimeId());
    }

}
