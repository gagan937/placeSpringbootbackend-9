package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ForgotPasswordRequest;
import com.example.demo.dto.ResetPasswordRequest;
import com.example.demo.dto.VerifyOtp;
import com.example.demo.service.PasswordResetService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "${cors.allowed-origin}")
public class PasswordResetController {
	
	 @Autowired
	  private PasswordResetService passwordResetService;

	    @PostMapping("/forgot-password")
	    public String forgotPassword(@RequestBody ForgotPasswordRequest request) {
	    	
	        return passwordResetService.sendOtp(request.getEmail());
	    }
	    
	    @PostMapping("/verify-otp")
	    public String varifyOtp(@RequestBody VerifyOtp verifyOtpRequest) {
	    	   
	    	return passwordResetService.verifyOtp(verifyOtpRequest.getOtp());
	    }
	    
	    
	 @PostMapping("/reset-Password")
	 public String resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
		 
		         
		  return passwordResetService.resetPassword(
	                resetPasswordRequest.getOtp(),
	                resetPasswordRequest.getNewPassword(),
	                resetPasswordRequest.getConfirmPassword());
	 }
 
	  
}
