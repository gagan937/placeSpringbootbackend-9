package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pending_users")
@Getter
@Setter
public class PendingUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String userId;
    private String username;
    private String email;
    private String password;
    private Long phoneNumber;
    private String role;
    private String verificationToken;
}