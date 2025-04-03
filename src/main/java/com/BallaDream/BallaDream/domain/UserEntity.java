package com.BallaDream.BallaDream.domain;

import com.BallaDream.BallaDream.constants.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Table(name = "users")
@Entity
@Setter
@Getter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;
    private String password;

    private String role;

    public UserEntity() {
    }

    public UserEntity(String username, String role) {
        this.username = username;
        this.role = role;
    }
}

