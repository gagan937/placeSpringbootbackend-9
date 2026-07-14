package com.example.demo.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="password_reset_tokens")
@Getter
@Setter
public class PasswordResetToken {
	
	
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Integer id;
	 
	 private String email;
	 private String otp;
	 @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	 private LocalDateTime expiryTime;
	 private boolean verified = false;

}
