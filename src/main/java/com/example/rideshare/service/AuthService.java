package com.example.rideshare.service;

import com.example.rideshare.dto.LoginRequest;
import com.example.rideshare.dto.RegisterRequest;
import com.example.rideshare.model.User;

public interface AuthService {
    User register(RegisterRequest request);
    String login(LoginRequest request);
}

