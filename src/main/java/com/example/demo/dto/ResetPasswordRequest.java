package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {

	   private String otp;
	   private String newPassword;
	   private String confirmPassword;
	   
}
