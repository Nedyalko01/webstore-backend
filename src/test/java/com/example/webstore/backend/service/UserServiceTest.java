package com.example.webstore.backend.service;

import com.example.webstore.backend.api.model.RegistrationRequest;
import com.example.webstore.backend.exception.UserAlreadyExistsException;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.servlet.Registration;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {

    @RegisterExtension
    private static GreenMailExtension greenMailExtension = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("springboot", "secret"))
            .withPerMethodLifecycle(true);

    @Autowired
    private UserService userService;

    @Test
    @Transactional
    public void TestRegisterUser() throws MessagingException {
        RegistrationRequest body = new RegistrationRequest();

        body.setUsername("UserA");
        body.setEmail("UserServiceTest$testRegisterUser@junit.com");
        body.setFirstName("FirstName");
        body.setLastName("LastName");
        body.setPassword("MySecretPassword123");

        Assertions.assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(body)
                , "Username should already be in use.");

        body.setUsername("UserServiceTest$testRegisterUser");
        body.setEmail("UserA@junit.com");

        Assertions.assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(body)
                , "Email should already be in use.");

        body.setEmail("UserServiceTest$testRegisterUser@junit.com");
        Assertions.assertDoesNotThrow(() -> userService.registerUser(body)
                , "User should register successfully.");

        Assertions.assertEquals(body.getEmail(), greenMailExtension.getReceivedMessages()[0]
                .getRecipients(Message.RecipientType.TO)[0].toString());
    }
}
