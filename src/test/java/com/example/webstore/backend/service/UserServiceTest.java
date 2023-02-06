package com.example.webstore.backend.service;

import com.example.webstore.backend.api.model.LoginBody;
import com.example.webstore.backend.api.model.RegistrationRequest;
import com.example.webstore.backend.exception.EmailFailureException;
import com.example.webstore.backend.exception.EmailNotFoundException;
import com.example.webstore.backend.exception.UserAlreadyExistsException;
import com.example.webstore.backend.exception.UserNotVerifiedException;
import com.example.webstore.backend.model.VerificationToken;
import com.example.webstore.backend.model.dto.VerificationTokenDAO;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceTest {

    @RegisterExtension
    private static GreenMailExtension greenMailExtension = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("springboot", "secret"))
            .withPerMethodLifecycle(true);

    @Autowired
    private UserService userService;
    @Autowired
    private VerificationTokenDAO verificationTokenDAO;

    @Test
    @Transactional
    public void testRegisterUser() throws MessagingException {
        RegistrationRequest body = new RegistrationRequest();

        body.setUsername("UserA");
        body.setEmail("UserServiceTest$testRegisterUser@junit.com");
        body.setFirstName("FirstName");
        body.setLastName("LastName");
        body.setPassword("MySecretPassword123");

        Assertions.assertThrows(UserAlreadyExistsException.class,
                () -> userService.registerUser(body), "Username should already be in use.");

        body.setUsername("UserServiceTest$testRegisterUser");
        body.setEmail("UserA@junit.com");

        Assertions.assertThrows(UserAlreadyExistsException.class,
                () -> userService.registerUser(body)
                , "Email should already be in use.");

        body.setEmail("UserServiceTest$testRegisterUser@junit.com");
        Assertions.assertDoesNotThrow(() -> userService.registerUser(body), "User should register successfully.");

        Assertions.assertEquals(body.getEmail(), greenMailExtension.getReceivedMessages()[0]
                .getRecipients(Message.RecipientType.TO)[0].toString());
    }

    @Test
    @Transactional
    public void testLoginUser() throws UserNotVerifiedException, EmailFailureException {

        LoginBody body = new LoginBody();

        body.setUsername("UserA-NotExists");
        body.setPassword("PasswordA123-Bad password");
        Assertions.assertNull(userService.loginUser(body), "The user should nto exists");

        body.setUsername("UserA");
        Assertions.assertNull(userService.loginUser(body), "The password should be incorrect");

        body.setPassword("PasswordA123");
        Assertions.assertNotNull(userService.loginUser(body), "The user should login successfully");

        body.setUsername("UserB");
        body.setPassword("PasswordB123");

        try {
            userService.loginUser(body);
            Assertions.assertTrue(false, "The user should not email verified");
        } catch (UserNotVerifiedException ex) {
            Assertions.assertTrue(ex.isNewEmailSend(), ("Email verification should be send"));
            Assertions.assertEquals(1, greenMailExtension.getReceivedMessages().length);
        }
        try {
            userService.loginUser(body);
            Assertions.assertTrue(false, "The user should not email verified");
        } catch (UserNotVerifiedException ex) {
            Assertions.assertFalse(ex.isNewEmailSend(), ("Email verification should not be resend"));
            Assertions.assertEquals(1, greenMailExtension.getReceivedMessages().length);
        }
    }

    @Test
    @Transactional
    public void tesVerifyUser() throws EmailFailureException {
        Assertions.assertFalse(userService.verifyUser("Bad Token"), "Token is bad or does not exist should return false");
        LoginBody body = new LoginBody();
        body.setUsername("UserB");
        body.setPassword("PasswordB123");
        try {
            userService.loginUser(body);
            Assertions.assertTrue(false, "The user should not email verified");
        } catch (UserNotVerifiedException ex) {
            List<VerificationToken> tokens = verificationTokenDAO.findByUser_IdOrderByIdDesc(2L);
            String token = tokens.get(0).getToken();
            Assertions.assertTrue(userService.verifyUser(token), "Token should be valid");
            Assertions.assertNotNull(body, "The user should now be verified");
        }
    }

    @Test
    @Transactional
    public void forgotPassword() throws MessagingException {
        Assertions.assertThrows(EmailNotFoundException.class, () -> userService.forgotPassword("UserNotExists@junit.comn"));

        Assertions.assertDoesNotThrow(() -> userService.forgotPassword("UserA@junit.com"));

        Assertions.assertEquals("UserA@junit.com",
                greenMailExtension.getReceivedMessages()[0]
                        .getRecipients(Message.RecipientType.TO)[0].toString());

    }
}