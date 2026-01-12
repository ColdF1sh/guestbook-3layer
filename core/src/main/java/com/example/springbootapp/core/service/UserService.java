package com.example.springbootapp.core.service;

import com.example.springbootapp.core.dto.RegistrationRequest;
import com.example.springbootapp.core.model.User;

import java.util.Optional;

public interface UserService {
    User registerUser(RegistrationRequest request);

    Optional<User> findByEmail(String email);
}
