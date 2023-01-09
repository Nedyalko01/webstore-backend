package com.example.webstore.backend.service;

import com.example.webstore.backend.api.model.RegistrationRequest;
import com.example.webstore.backend.exception.UserAlreadyExistsException;
import com.example.webstore.backend.model.LocalUser;
import com.example.webstore.backend.model.dto.LocalUserDAO;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final LocalUserDAO localUserDAO;

    public UserService(LocalUserDAO localUserDAO) {
        this.localUserDAO = localUserDAO;
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
        user.setPassword(registration.getPassword());

        user = localUserDAO.save(user);
        return user;
    }

    public List<LocalUser> getAllUsers() {
        return localUserDAO.findAll();

    }
}