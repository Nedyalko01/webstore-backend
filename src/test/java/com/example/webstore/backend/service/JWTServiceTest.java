package com.example.webstore.backend.service;

import com.example.webstore.backend.model.LocalUser;
import com.example.webstore.backend.model.dto.LocalUserDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
public class JWTServiceTest {

    @Autowired
    private JWTService jwtService;
    @Autowired
    private LocalUserDAO localUserDAO;

    @Test
    public void testVerificationTokenNotUsableForLogin() {

        LocalUser user = localUserDAO.findByUsernameIgnoreCase("UserA").get();
        String token = jwtService.generateVerificationJWT(user);

        Assertions.assertNull(jwtService.getUsername(token), "Verification token should not contain username");

    }

    @Test
    public void testAuthTokenReturnsUsername() {

        LocalUser user = localUserDAO.findByUsernameIgnoreCase("UserA").get();
        String token = jwtService.generateJWT(user);

        Assertions.assertEquals(user.getUsername(), jwtService.getUsername(token), "Token for auth should contain users username");
    }
}
