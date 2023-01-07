package com.example.webstore.backend.api.controller;

import com.example.webstore.backend.api.model.RegistrationRequest;
import com.example.webstore.backend.exception.UserAlreadyExistsException;
import com.example.webstore.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity RegisterUser(@Valid @RequestBody RegistrationRequest request) {

        try {
            userService.registerUser(request);
            return ResponseEntity.ok().build();
        }  catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }



    }
}
