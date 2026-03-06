package com.collegeerp.service;

import com.collegeerp.dto.request.LoginRequest;
import com.collegeerp.dto.request.UserCreateRequest;
import com.collegeerp.dto.response.AuthResponse;
import com.collegeerp.dto.response.UserResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);

    UserResponse createUser(UserCreateRequest request);
}
