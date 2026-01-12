package com.example.springbootapp.core.dto;

public class RegistrationRequest {

    private final String email;
    private final String password;

    public RegistrationRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
