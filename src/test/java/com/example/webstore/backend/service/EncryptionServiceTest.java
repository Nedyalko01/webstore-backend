package com.example.webstore.backend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EncryptionServiceTest {

        @Autowired
        private EncryptionService encryptionService;

        @Test
        public void PasswordEncryptionTest() {
            String password = "PasswordIsSecret!123";
            String hash = encryptionService.encryptPassword(password);

            Assertions.assertTrue(encryptionService.verifyPassword(password, hash), "Hashed password should math original");
            Assertions.assertFalse(encryptionService.verifyPassword(password + "Sike!", hash), "Altered password should not be valid") ;

        }
    }

