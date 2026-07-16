package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="users")
@Getter
@Setter
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String userId;
	private String username;
	private String email;
	private String password;
	private Long phoneNumber;
	private String role;
	private boolean enabled =false;  // this account is active or not
    private String verificationToken;   // for verify
    
    
    private String resetOtp;
    private LocalDateTime otpExpiry;

}
