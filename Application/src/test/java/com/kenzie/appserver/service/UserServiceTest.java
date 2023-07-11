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
        favoriteAnime.add("11");

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

        String animeId = "12";
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

        List<String> updatedList = new ArrayList<>();
        updatedList.add("11");
        updatedList.add(animeId);



        service.addNewFavorite("displayName", anime.getAnimeId());

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

    @Test
    void follow_happyCase() {
        String userId = randomUUID().toString();
        String userId2 = randomUUID().toString();
        User user = new User(new ArrayList<>(), new ArrayList<>(), "email", userId, new ArrayList<>(),
                "fullName", "displayName", 27, "bio");
        User user2 = new User(new ArrayList<>(), new ArrayList<>(), "email2", userId2, new ArrayList<>(),
                "name2", "displayName2", 27, "bio2");

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

        UserRecord userRecord2 = new UserRecord();
        userRecord2.setUserId(user2.getUserId());
        userRecord2.setFollowers(user2.getFollowers());
        userRecord2.setFollowing(user2.getFollowing());
        userRecord2.setFavoriteAnime(user2.getFavoriteAnime());
        userRecord2.setEmail(user2.getEmail());
        userRecord2.setFullName(user2.getFullName());
        userRecord2.setAge(user2.getAge());
        userRecord2.setDisplayName(user2.getDisplayName());
        userRecord2.setBio(user2.getBio());
        repository.save(userRecord2);

        when(repository.findByDisplayName(userRecord.getDisplayName())).thenReturn(Optional.of(userRecord));
        when(repository.findByDisplayName(userRecord2.getDisplayName())).thenReturn(Optional.of(userRecord2));

        List<String> updatedFollowers = service.follow(user.getDisplayName(), user2.getDisplayName());

        verify(repository, times(1)).findByDisplayName(userRecord.getDisplayName());
        verify(repository, times(1)).findByDisplayName(userRecord2.getDisplayName());
        verify(repository, times(2)).save(userRecord);
        verify(repository, times(2)).save(userRecord2);

        assertEquals(updatedFollowers, user.getFollowing(), "friend was properly followed");
        assertEquals(user.getFollowing().get(0), "displayName2", "user's following should consist of friend's display name");
        assertEquals(user2.getFollowers().get(0), "displayName", "user should be added to friend's followers");
    }

    @Test
    void follow_sameDisplayName_throwsException() {
        String userId = randomUUID().toString();
        String userId2 = randomUUID().toString();
        User user = new User(new ArrayList<>(), new ArrayList<>(), "email", userId, new ArrayList<>(),
                "fullName", "displayName", 27, "bio");
        User user2 = new User(new ArrayList<>(), new ArrayList<>(), "email2", userId2, new ArrayList<>(),
                "name2", "displayName", 27, "bio2");


        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            service.follow(user.getDisplayName(), user2.getDisplayName());
        });
    }

    @Test
    void follow_nullFollowing_stillFollows() {
        String userId = randomUUID().toString();
        String userId2 = randomUUID().toString();
        User user = new User(new ArrayList<>(), null, "email", userId, new ArrayList<>(),
                "fullName", "displayName", 27, "bio");
        User user2 = new User(new ArrayList<>(), new ArrayList<>(), "email2", userId2, new ArrayList<>(),
                "name2", "displayName2", 27, "bio2");

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

        UserRecord userRecord2 = new UserRecord();
        userRecord2.setUserId(user2.getUserId());
        userRecord2.setFollowers(user2.getFollowers());
        userRecord2.setFollowing(user2.getFollowing());
        userRecord2.setFavoriteAnime(user2.getFavoriteAnime());
        userRecord2.setEmail(user2.getEmail());
        userRecord2.setFullName(user2.getFullName());
        userRecord2.setAge(user2.getAge());
        userRecord2.setDisplayName(user2.getDisplayName());
        userRecord2.setBio(user2.getBio());
        repository.save(userRecord2);

        when(repository.findByDisplayName(userRecord.getDisplayName())).thenReturn(Optional.of(userRecord));
        when(repository.findByDisplayName(userRecord2.getDisplayName())).thenReturn(Optional.of(userRecord2));

        List<String> updatedFollowers = service.follow(user.getDisplayName(), user2.getDisplayName());

        verify(repository, times(1)).findByDisplayName(userRecord.getDisplayName());
        verify(repository, times(1)).findByDisplayName(userRecord2.getDisplayName());
        verify(repository, times(2)).save(userRecord);
        verify(repository, times(2)).save(userRecord2);

        assertEquals(updatedFollowers, userRecord.getFollowing(), "friend was properly followed");
        assertEquals(userRecord.getFollowing().get(0), "displayName2", "user's following should consist of friend's display name");
        assertEquals(user2.getFollowers().get(0), "displayName", "user should be added to friend's followers");
    }

    @Test
    void unfollow_happyCase() {
        String userId = randomUUID().toString();
        String userId2 = randomUUID().toString();
        List<String> following = new ArrayList<>();
        following.add("displayName2");
        List<String> followers = new ArrayList<>();
        followers.add("displayName");
        User user = new User(new ArrayList<>(), following, "email", userId, new ArrayList<>(),
                "fullName", "displayName", 27, "bio");
        User user2 = new User(followers, new ArrayList<>(), "email2", userId2, new ArrayList<>(),
                "name2", "displayName2", 27, "bio2");

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

        UserRecord userRecord2 = new UserRecord();
        userRecord2.setUserId(user2.getUserId());
        userRecord2.setFollowers(user2.getFollowers());
        userRecord2.setFollowing(user2.getFollowing());
        userRecord2.setFavoriteAnime(user2.getFavoriteAnime());
        userRecord2.setEmail(user2.getEmail());
        userRecord2.setFullName(user2.getFullName());
        userRecord2.setAge(user2.getAge());
        userRecord2.setDisplayName(user2.getDisplayName());
        userRecord2.setBio(user2.getBio());
        repository.save(userRecord2);

        when(repository.findByDisplayName(userRecord.getDisplayName())).thenReturn(Optional.of(userRecord));
        when(repository.findByDisplayName(userRecord2.getDisplayName())).thenReturn(Optional.of(userRecord2));

        service.unfollow(user.getDisplayName(), user2.getDisplayName());

        verify(repository, times(1)).findByDisplayName(userRecord.getDisplayName());
        verify(repository, times(1)).findByDisplayName(userRecord2.getDisplayName());
        verify(repository, times(2)).save(userRecord);
        verify(repository, times(2)).save(userRecord2);

        assertEquals(userRecord.getFollowing().size(), 0, "friend was properly unfollowed");
        assertEquals(userRecord2.getFollowers().size(), 0, "user's following should now be an empty list");
    }

    @Test
    void unfollow_sameName_throwsException() {
        String userId = randomUUID().toString();
        String userId2 = randomUUID().toString();
        User user = new User(new ArrayList<>(), new ArrayList<>(), "email", userId, new ArrayList<>(),
                "fullName", "displayName", 27, "bio");
        User user2 = new User(new ArrayList<>(), new ArrayList<>(), "email2", userId2, new ArrayList<>(),
                "name2", "displayName", 27, "bio2");


        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            service.unfollow(user.getDisplayName(), user2.getDisplayName());
        });
    }
    @Test
    void findDisplayByEmail_validEmail_returnsDisplayName() {
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

        when(repository.findByEmail("email")).thenReturn(Optional.of(userRecord));
        String foundName = service.findDisplayNameByEmail("email");

        verify(repository).findByEmail("email");
        assertEquals(foundName, userRecord.getDisplayName());
    }
}