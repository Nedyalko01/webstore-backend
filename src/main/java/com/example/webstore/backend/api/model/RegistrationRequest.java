package com.example.webstore.backend.api.model;

import jakarta.validation.constraints.*;

public class RegistrationRequest {

    @NotNull
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;
    @NotNull
    @NotBlank
    @Size(min = 3, max = 32)
    //@Pattern(regexp = "/[0-9a-zA-Z]{6,}/")
    private String password;
    @NotNull
    @NotBlank
    @Email
    private String email;
    @NotNull
    @NotBlank
    private String firstName;
    @NotNull
    @NotBlank
    private String lastName;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
