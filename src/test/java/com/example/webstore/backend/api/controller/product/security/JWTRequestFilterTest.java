package com.example.webstore.backend.api.controller.product.security;


import com.example.webstore.backend.model.LocalUser;
import com.example.webstore.backend.model.dto.LocalUserDAO;
import com.example.webstore.backend.service.JWTService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class JWTRequestFilterTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private LocalUserDAO localUserDAO;

    private static final String AUTHENTICATED_PATH = "/auth/v1";

    @Test
    public void unauthenticatedRequest() throws Exception {
        mvc.perform(get(AUTHENTICATED_PATH)).andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void badTokenTest() throws Exception {
        mvc.perform(get(AUTHENTICATED_PATH).header("Authorization", "BadTokenItsNotValid"))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
        mvc.perform(get(AUTHENTICATED_PATH).header("Authorization", "Bearer BadTokenItsNotValid"))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void unverifiedUserTest() throws Exception {

        LocalUser user = localUserDAO.findByUsernameIgnoreCase("UserB").get();

        String token = jwtService.generateJWT(user);

        mvc.perform(get(AUTHENTICATED_PATH).header("Authorization", "Bearer " + token))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void successfulTest() throws Exception {

        LocalUser user = localUserDAO.findByUsernameIgnoreCase("UserA").get();

        String token = jwtService.generateJWT(user);

        mvc.perform(get(AUTHENTICATED_PATH).header("Authorization", "Bearer " + token))
                .andExpect(status().is(HttpStatus.OK.value()));
    }
}

