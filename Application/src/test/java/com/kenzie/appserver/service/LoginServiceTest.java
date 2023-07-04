package com.kenzie.appserver.service;

import com.kenzie.appserver.exceptions.NicknameAlreadyExistsException;
import com.kenzie.appserver.repositories.LoginRepository;
import com.kenzie.appserver.repositories.UserRepository;
import com.kenzie.appserver.repositories.model.LoginRecord;
import com.kenzie.appserver.repositories.model.UserRecord;
import com.kenzie.appserver.service.model.Login;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginServiceTest {

    @Mock
    private LoginRepository loginRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoginService loginService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        loginService = new LoginService(loginRepository, userRepository);

    }

    @Test
    void createLogin_NewEmailAndPassword_Returns200() {
        String email = "test@example.com";
        String password = "password";
        String nickname = "testuser";
        String userId = UUID.randomUUID().toString();

        when(loginRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(userRepository.findByDisplayName(nickname)).thenReturn(Optional.empty());

        int result = loginService.createLogin(email, password, nickname);

        assertEquals(200, result);
        verify(loginRepository, times(1)).save(any(LoginRecord.class));
        verify(userRepository, times(1)).save(any(UserRecord.class));
    }

    @Test
    void createLogin_ExistingEmail_Returns444() {
        String email = "test@example.com";
        String password = "password";
        String nickname = "testuser";
        LoginRecord existingRecord = new LoginRecord();

        when(loginRepository.findByEmail(email)).thenReturn(Optional.of(existingRecord));

        int result = loginService.createLogin(email, password, nickname);

        assertEquals(444, result);
        verify(loginRepository, never()).save(any(LoginRecord.class));
        verify(userRepository, never()).save(any(UserRecord.class));
    }

    @Test
    void createLogin_ExistingNickname_ThrowsNicknameAlreadyExistsException() {
        String email = "test@example.com";
        String password = "password";
        String nickname = "testuser";
        UserRecord existingUser = new UserRecord();

        when(loginRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(userRepository.findByDisplayName(nickname)).thenReturn(Optional.of(existingUser));

        assertThrows(NicknameAlreadyExistsException.class, () ->
                loginService.createLogin(email, password, nickname));

        verify(loginRepository, never()).save(any(LoginRecord.class));
        verify(userRepository, never()).save(any(UserRecord.class));
    }

    @Test
    void login_ValidEmail_ReturnsPassword() {
        String email = "test@example.com";
        String password = "password";
        LoginRecord loginRecord = new LoginRecord();
        loginRecord.setEmail(email);
        loginRecord.setPassword(password);

        when(loginRepository.findByEmail(email)).thenReturn(Optional.of(loginRecord));

        String result = loginService.login(email);

        assertEquals(password, result);
    }

    @Test
    void login_NonexistentEmail_ReturnsNull() {
        String email = "test@example.com";

        when(loginRepository.findByEmail(email)).thenReturn(Optional.empty());

        String result = loginService.login(email);

        assertNull(result);
    }

    @Test
    void deleteLoginByEmail_ExistingEmailAndPassword_ReturnsTrue() {
        String email = "test@example.com";
        String password = "password";
        LoginRecord loginRecord = new LoginRecord();
        loginRecord.setEmail(email);
        loginRecord.setPassword(password);

        when(loginRepository.findByEmail(email)).thenReturn(Optional.of(loginRecord));

        boolean result = loginService.deleteLoginByEmail(email, password);

        assertTrue(result);
        verify(loginRepository, times(1)).delete(loginRecord);
    }

    @Test
    void deleteLoginByEmail_NonexistentEmail_ReturnsFalse() {
        String email = "test@example.com";
        String password = "password";

        when(loginRepository.findByEmail(email)).thenReturn(Optional.empty());

        boolean result = loginService.deleteLoginByEmail(email, password);

        assertFalse(result);
        verify(loginRepository, never()).delete(any(LoginRecord.class));
    }

    @Test
    void deleteLoginByEmail_InvalidPassword_ReturnsFalse() {
        String email = "test@example.com";
        String password = "password";
        LoginRecord loginRecord = new LoginRecord();
        loginRecord.setEmail(email);
        loginRecord.setPassword("wrong_password");

        when(loginRepository.findByEmail(email)).thenReturn(Optional.of(loginRecord));

        boolean result = loginService.deleteLoginByEmail(email, password);

        assertFalse(result);
        verify(loginRepository, never()).delete(any(LoginRecord.class));
    }

    @Test
    void updatePasswordByEmail_ExistingEmailAndPassword_ReturnsTrue() {
        String email = "test@example.com";
        String newPassword = "new_password";
        LoginRecord loginRecord = new LoginRecord();
        loginRecord.setEmail(email);


        when(loginRepository.findByEmail(email)).thenReturn(Optional.of(loginRecord));

        boolean result = loginService.updatePasswordByEmail(email, newPassword);

        assertTrue(result);
        verify(loginRepository, times(1)).save(loginRecord);
        assertEquals(newPassword, loginRecord.getPassword());
    }

    @Test
    void updatePasswordByEmail_NonexistentEmail_ReturnsFalse() {
        String email = "test@example.com";
        String newPassword = "new_password";

        when(loginRepository.findByEmail(email)).thenReturn(Optional.empty());

        boolean result = loginService.updatePasswordByEmail(email, newPassword);

        assertFalse(result);
        verify(loginRepository, never()).save(any(LoginRecord.class));
    }


    @Test
    void updateEmailByEmail_ExistingEmailAndPassword_ReturnsTrue() {
        String email = "test@example.com";
        String updatedEmail = "new@example.com";

        LoginRecord loginRecord = new LoginRecord();
        loginRecord.setEmail(email);


        when(loginRepository.findByEmail(email)).thenReturn(Optional.of(loginRecord));

        boolean result = loginService.updateEmailByEmail(email, updatedEmail);

        assertTrue(result);
        verify(loginRepository, times(1)).save(loginRecord);
        assertEquals(updatedEmail, loginRecord.getEmail());
    }

    @Test
    void updateEmailByEmail_NonexistentEmail_ReturnsFalse() {
        String email = "test@example.com";
        String updatedEmail = "new@example.com";


        when(loginRepository.findByEmail(email)).thenReturn(Optional.empty());

        boolean result = loginService.updateEmailByEmail(email, updatedEmail);

        assertFalse(result);
        verify(loginRepository, never()).save(any(LoginRecord.class));
    }

    @Test
    void getUserIdByEmail_ExistingEmail_ReturnsUserId() {
        String email = "test@example.com";
        String userId = UUID.randomUUID().toString();
        LoginRecord loginRecord = new LoginRecord();
        loginRecord.setEmail(email);
        loginRecord.setUserId(userId);

        when(loginRepository.findByEmail(email)).thenReturn(Optional.of(loginRecord));

        String result = loginService.getUserIdByEmail(email);

        assertEquals(userId, result);
    }

    @Test
    void getUserIdByEmail_NonexistentEmail_ReturnsNull() {
        String email = "test@example.com";

        when(loginRepository.findByEmail(email)).thenReturn(Optional.empty());

        String result = loginService.getUserIdByEmail(email);

        assertNull(result);
    }

}
