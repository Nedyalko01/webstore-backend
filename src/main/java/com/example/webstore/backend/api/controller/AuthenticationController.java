package com.example.webstore.backend.api.controller;

import com.example.webstore.backend.api.model.LoginBody;
import com.example.webstore.backend.api.model.LoginResponse;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
    public ResponseEntity<LocalUser> RegisterUser(@Valid @RequestBody RegistrationRequest request) {

        try {
            userService.registerUser(request);
            return ResponseEntity.ok().build();
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginBody loginBody) {
        String jwt = userService.loginUser(loginBody);

        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            LoginResponse response = new LoginResponse();
            response.setJwt(jwt);
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/v1")
    public LocalUser getLoggedInUserProfile(@AuthenticationPrincipal LocalUser user) {
        return user;

        // SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        try {
            localUserDAO.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            throw new NoSuchElementException("No such Address");
        }
    }

    @GetMapping
    public List<LocalUser> getAllUsers() {
        return userService.getAllUsers();
    }
}