package com.example.webstore.backend.api.controller;

import com.example.webstore.backend.api.model.RegistrationRequest;
import com.example.webstore.backend.exception.UserAlreadyExistsException;
import com.example.webstore.backend.model.Address;
import com.example.webstore.backend.model.LocalUser;
import com.example.webstore.backend.model.dto.LocalUserDAO;
import com.example.webstore.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {


    private final UserService userService;
    private final LocalUserDAO localUserDAO;

    @Autowired
    public AuthenticationController(UserService userService, LocalUserDAO localUserDAO) {
        this.userService = userService;
        this.localUserDAO = localUserDAO;
    }

    @PostMapping("/register")
    public ResponseEntity RegisterUser(@Valid @RequestBody RegistrationRequest request) {

        try {
            userService.registerUser(request);
            return ResponseEntity.ok().build();
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

    }

    @GetMapping
    public List<LocalUser> getAllUsers() {
        return userService.getAllUsers();
    }
}