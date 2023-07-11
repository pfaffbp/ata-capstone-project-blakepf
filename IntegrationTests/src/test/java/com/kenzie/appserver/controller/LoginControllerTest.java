package com.kenzie.appserver.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.LoginController;
import com.kenzie.appserver.controller.model.*;
import com.kenzie.appserver.service.LoginService;
import com.kenzie.appserver.service.model.Login;
import com.kenzie.appserver.exceptions.NicknameAlreadyExistsException;
import com.kenzie.appserver.repositories.model.LoginRecord;
import com.kenzie.appserver.repositories.model.UserRecord;
import com.kenzie.appserver.repositories.LoginRepository;
import com.kenzie.appserver.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Optional;
import java.util.UUID;


import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@IntegrationTest
class LoginControllerTest {


    @Autowired
    private MockMvc mvc;


    @Mock
    private LoginRepository loginRepository;


    @Mock
    private UserRepository userRepository;


    @InjectMocks
    private LoginService loginService;


    @InjectMocks
    private LoginController loginController;


    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        loginService = new LoginService(loginRepository, userRepository);
        loginController = new LoginController(loginService);
        objectMapper = new ObjectMapper();
    }




    @Test
    void testCreateLogin_Successful() throws Exception {
        String email = "syfy@example.com";
        String password = "password";
        String nickname = "syfyTest";
        String userId = UUID.randomUUID().toString();


        LoginCreateRequest request = new LoginCreateRequest();
        request.setEmail(email);
        request.setPassword(password);
        request.setNickname(nickname);


        when(loginRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(userRepository.findByDisplayName(nickname)).thenReturn(Optional.empty());


        mvc.perform(post("/login/createLogin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful());
    }


//    @Test
//    void testUpdatePasswordByEmail_InvalidEmail_ReturnsConflict() throws Exception {
//        String email = "test@example.com";
//        String password = "password";
//        String newPassword = "newpassword";
//
//
//        when(loginRepository.findByEmail(email)).thenReturn(Optional.ofNullable(null));
//
//
//        LoginUpdatePasswordRequest request = new LoginUpdatePasswordRequest();
//        request.setEmail(email);
//        request.setNewPassword(newPassword);
//
//
//        mvc.perform(put("/login/changePassword")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().is(409));
//    }


    @Test
    void testDeleteLoginByEmail_InvalidEmail_ReturnsConflict() throws Exception {
        String email = "test@example.com";
        String password = "password";


        when(loginRepository.findByEmail(email)).thenReturn(Optional.empty());


        LoginDeleteRequest request = new LoginDeleteRequest();
        request.setEmail(email);
        request.setPassword(password);


        mvc.perform(delete("/login/deleteAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }






    @Test
    void testGetUserIdByEmail_NonExistingEmail_ReturnsNotFound() throws Exception {
        String email = "test@example.com";


        when(loginRepository.findByEmail(email)).thenReturn(Optional.empty());


        mvc.perform(get("/login/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
