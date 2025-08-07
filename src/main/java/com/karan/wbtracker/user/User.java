package com.karan.wbtracker.user;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
public class User {

    @Setter
    @Getter
    @Id
    private String id;
    private String username;
    @Setter
    @Getter
    private String email;
    @Setter
    @Getter
    private String password;

    // Constructors, Getters, and Setters

    public User(String id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public User() {
    }
}