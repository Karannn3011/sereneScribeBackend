package com.karan.wbtracker.user;

import lombok.Data;

@Data
public class UserDto {
    private String id;
    private String username;
    private String email;

    // A simple constructor to easily convert a User to a UserDto
    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }
}