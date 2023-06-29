package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.LoginRepository;
import com.kenzie.appserver.repositories.model.LoginRecord;
import com.kenzie.appserver.service.model.Login;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginServiceTest {


    @Mock
    private LoginRepository loginRepository;

    private LoginService loginService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        loginService = new LoginService(loginRepository);
    }

    @Test
    void createLogin_NewEmailAndPassword_ReturnsTrue() {
        String email = "test@example.com";
        String password = "password";

        when(loginRepository.findByEmail(email)).thenReturn(Optional.empty());

        boolean result = loginService.createLogin(email, password);

        assertTrue(result);
        verify(loginRepository, times(1)).save(any(LoginRecord.class));
    }

    @Test
    void createLogin_ExistingEmailAndPassword_ReturnsFalse() {
        String email = "test@example.com";
        String password = "password";
        LoginRecord existingRecord = new LoginRecord();

        when(loginRepository.findByEmail(email)).thenReturn(Optional.of(existingRecord));

        boolean result = loginService.createLogin(email, password);

        assertFalse(result);
        verify(loginRepository, never()).save(any(LoginRecord.class));
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
        String currentPassword = "password";
        String newPassword = "new_password";
        LoginRecord loginRecord = new LoginRecord();
        loginRecord.setEmail(email);
        loginRecord.setPassword(currentPassword);

        when(loginRepository.findByEmail(email)).thenReturn(Optional.of(loginRecord));

        boolean result = loginService.updatePasswordByEmail(email, currentPassword, newPassword);

        assertTrue(result);
        verify(loginRepository, times(1)).save(loginRecord);
        assertEquals(newPassword, loginRecord.getPassword());
    }

    @Test
    void updatePasswordByEmail_NonexistentEmail_ReturnsFalse() {
        String email = "test@example.com";
        String currentPassword = "password";
        String newPassword = "new_password";

        when(loginRepository.findByEmail(email)).thenReturn(Optional.empty());

        boolean result = loginService.updatePasswordByEmail(email, currentPassword, newPassword);

        assertFalse(result);
        verify(loginRepository, never()).save(any(LoginRecord.class));
    }

    @Test
    void updatePasswordByEmail_InvalidPassword_ReturnsFalse() {
        String email = "test@example.com";
        String currentPassword = "password";
        String newPassword = "new_password";
        LoginRecord loginRecord = new LoginRecord();
        loginRecord.setEmail(email);
        loginRecord.setPassword("wrong_password");

        when(loginRepository.findByEmail(email)).thenReturn(Optional.of(loginRecord));

        boolean result = loginService.updatePasswordByEmail(email, currentPassword, newPassword);

        assertFalse(result);
        verify(loginRepository, never()).save(any(LoginRecord.class));
    }

    @Test
    void updateEmailByEmail_ExistingEmailAndPassword_ReturnsTrue() {
        String email = "test@example.com";
        String updatedEmail = "new@example.com";
        String password = "password";
        LoginRecord loginRecord = new LoginRecord();
        loginRecord.setEmail(email);
        loginRecord.setPassword(password);

        when(loginRepository.findByEmail(email)).thenReturn(Optional.of(loginRecord));

        boolean result = loginService.updateEmailByEmail(email, updatedEmail, password);

        assertTrue(result);
        verify(loginRepository, times(1)).save(loginRecord);
        assertEquals(updatedEmail, loginRecord.getEmail());
    }

    @Test
    void updateEmailByEmail_NonexistentEmail_ReturnsFalse() {
        String email = "test@example.com";
        String updatedEmail = "new@example.com";
        String password = "password";

        when(loginRepository.findByEmail(email)).thenReturn(Optional.empty());

        boolean result = loginService.updateEmailByEmail(email, updatedEmail, password);

        assertFalse(result);
        verify(loginRepository, never()).save(any(LoginRecord.class));
    }

    @Test
    void updateEmailByEmail_InvalidPassword_ReturnsFalse() {
        String email = "test@example.com";
        String updatedEmail = "new@example.com";
        String password = "password";
        LoginRecord loginRecord = new LoginRecord();
        loginRecord.setEmail(email);
        loginRecord.setPassword("wrong_password");

        when(loginRepository.findByEmail(email)).thenReturn(Optional.of(loginRecord));

        boolean result = loginService.updateEmailByEmail(email, updatedEmail, password);

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