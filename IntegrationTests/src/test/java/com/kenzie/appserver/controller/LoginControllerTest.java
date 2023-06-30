package com.kenzie.appserver.controller;

import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.LoginController;
import com.kenzie.appserver.controller.model.*;
import com.kenzie.appserver.service.LoginService;
import com.kenzie.appserver.service.model.Login;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;



import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@IntegrationTest
class LoginControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private LoginService loginService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testCreateLogin() throws Exception {
        LoginCreateRequest request = new LoginCreateRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        mvc.perform(post("/login/createLogin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is(HttpStatus.ACCEPTED.value()));
    }


    @Test
    void testUpdatePasswordByEmail() throws Exception {
        LoginUpdatePasswordRequest request = new LoginUpdatePasswordRequest();
        request.setEmail("test@example.com");
        request.setNewPassword("newpassword");

        mvc.perform(put("/login/changePassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is(HttpStatus.ACCEPTED.value()));
    }


    @Test
    void testGetUserIdByEmail() throws Exception {
        String email = "test@example.com";

        mvc.perform(get("/login/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
