package com.karan.wbtracker.auth;

import com.karan.wbtracker.user.UserDto;

public class AuthResponse {
    private final String token;
     private final UserDto user;

    public AuthResponse(String token, UserDto user) {
        this.token = token;
        this.user = user;
    }

    // Getters
    public String getToken() {
        return token;
    }

    public UserDto getUser() {
        return user;
    }
}