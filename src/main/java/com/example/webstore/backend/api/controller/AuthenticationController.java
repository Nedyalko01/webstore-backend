package com.example.webstore.backend.api.controller;

import com.example.webstore.backend.api.model.LoginBody;
import com.example.webstore.backend.api.model.LoginResponse;
import com.example.webstore.backend.api.model.PasswordResetRequest;
import com.example.webstore.backend.api.model.RegistrationRequest;
import com.example.webstore.backend.exception.EmailFailureException;
import com.example.webstore.backend.exception.EmailNotFoundException;
import com.example.webstore.backend.exception.UserAlreadyExistsException;
import com.example.webstore.backend.exception.UserNotVerifiedException;
import com.example.webstore.backend.model.LocalUser;
import com.example.webstore.backend.model.dto.LocalUserDAO;
import com.example.webstore.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(value = "/register")
    public ResponseEntity<LocalUser> registerUser(@Valid @RequestBody RegistrationRequest request) {

        try {
            userService.registerUser(request);
            return ResponseEntity.ok().build();
        } catch (UserAlreadyExistsException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (EmailFailureException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginBody loginBody) {

        String jwt = null;

        try {
            jwt = userService.loginUser(loginBody);
        } catch (UserNotVerifiedException ex) {
            LoginResponse response = new LoginResponse();
            response.setSuccess(false);
            String reason = "USER_NOT_VERIFIED";

            if (ex.isNewEmailSend()) {
                reason += "EMAIL_RESEND";
            }
            response.setFailureReason(reason);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);

        } catch (EmailFailureException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            LoginResponse response = new LoginResponse();
            response.setJwt(jwt);
            response.setSuccess(true);
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping(value = "/verify")
    public ResponseEntity verifyEmail(@RequestParam String token) {

        if (userService.verifyUser(token)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping(value = "/access")
    public LocalUser getLoggedInUserProfile(@AuthenticationPrincipal LocalUser user) {
        return user;

        // SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @PostMapping(value = "/forgot")
    public ResponseEntity forgotPassword(@RequestParam String email) {
        try {
            userService.forgotPassword(email);
            return ResponseEntity.ok().build();
        } catch (EmailNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (EmailFailureException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PostMapping(value = "/reset")
    public ResponseEntity resetPassword(@Valid @RequestBody PasswordResetRequest request) {

        userService.resetPassword(request);

        return ResponseEntity.ok().build();
    }


    @GetMapping
    public List<LocalUser> getAllUsers() {
        return userService.getAllUsers();
    }
}