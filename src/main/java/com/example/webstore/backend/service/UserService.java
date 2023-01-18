package com.example.webstore.backend.service;

import com.example.webstore.backend.api.model.LoginBody;
import com.example.webstore.backend.api.model.RegistrationRequest;
import com.example.webstore.backend.exception.EmailFailureException;
import com.example.webstore.backend.exception.UserAlreadyExistsException;
import com.example.webstore.backend.model.LocalUser;
import com.example.webstore.backend.model.VerificationToken;
import com.example.webstore.backend.model.dto.LocalUserDAO;
import com.example.webstore.backend.model.dto.VerificationTokenDAO;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class UserService {

    private final LocalUserDAO localUserDAO;
    private final EncryptionService encryptionService;
    private final JWTService jwtService;
    private final EmailService emailService;
    private final VerificationTokenDAO verificationTokenDAO;

    public UserService(LocalUserDAO localUserDAO, EncryptionService encryptionService, JWTService jwtService, EmailService emailService, VerificationTokenDAO verificationTokenDAO) {
        this.localUserDAO = localUserDAO;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.verificationTokenDAO = verificationTokenDAO;
    }

    public LocalUser registerUser(RegistrationRequest registration) throws UserAlreadyExistsException, EmailFailureException {

        if (localUserDAO.findByEmailIgnoreCase(registration.getEmail()).isPresent()
                || localUserDAO.findByUsernameIgnoreCase(registration.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        LocalUser user = new LocalUser();
        user.setUsername(registration.getUsername());
        user.setEmail(registration.getEmail());
        user.setFirstName(registration.getFirstName());
        user.setLastName(registration.getLastName());
        user.setPassword(encryptionService.encryptPassword(registration.getPassword()));

        VerificationToken verificationToken = new VerificationToken();

        emailService.sendVerificationEmail(verificationToken);

        verificationTokenDAO.save(verificationToken);

        user = localUserDAO.save(user);
        return user;
    }


    private VerificationToken createVerificationToken(LocalUser user) {

        VerificationToken verificationToken = new VerificationToken();

        verificationToken.setToken(jwtService.generateJWT(user));
        verificationToken.setCreatedTimestamp(new Timestamp(System.currentTimeMillis()));
        verificationToken.setLocalUser(user);
        user.getVerificationTokens().add(verificationToken);
        return verificationToken;
    }


    public String loginUser(LoginBody loginBody) {
        Optional<LocalUser> optionalUser = localUserDAO.findByUsernameIgnoreCase(loginBody.getUsername());

        if (optionalUser.isPresent()) {
          LocalUser user = optionalUser.get();

          if (encryptionService.verifyPassword(loginBody.getPassword(), user.getPassword())) {
              return jwtService.generateJWT(user);
          }
        }
        return null;
    }

    public List<LocalUser> getAllUsers() {
        return localUserDAO.findAll();

    }
}