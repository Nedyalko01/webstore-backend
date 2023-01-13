package com.example.webstore.backend.service;

import com.example.webstore.backend.api.model.LoginBody;
import com.example.webstore.backend.api.model.RegistrationRequest;
import com.example.webstore.backend.exception.UserAlreadyExistsException;
import com.example.webstore.backend.model.LocalUser;
import com.example.webstore.backend.model.dto.LocalUserDAO;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final LocalUserDAO localUserDAO;

    private final EncryptionService encryptionService;

    private final JWTService jwtService;

    public UserService(LocalUserDAO localUserDAO, EncryptionService encryptionService, JWTService jwtService) {
        this.localUserDAO = localUserDAO;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
    }

    public LocalUser registerUser(RegistrationRequest registration) throws UserAlreadyExistsException {

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

        user = localUserDAO.save(user);
        return user;
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