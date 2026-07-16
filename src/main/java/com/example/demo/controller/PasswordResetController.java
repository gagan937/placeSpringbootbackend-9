package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        String result = passwordResetService.sendOtp(request.getEmail());
        if (result.contains("nahi hai") || result.contains("problem hui")) {
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody VerifyOtp verifyOtpRequest) {
        String result = passwordResetService.verifyOtp(verifyOtpRequest.getOtp());
        if (result.equals("OTP verified successfully")) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.badRequest().body(result);   // Invalid/expired OTP → 400
    }

    @PostMapping("/reset-Password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        String result = passwordResetService.resetPassword(
                resetPasswordRequest.getOtp(),
                resetPasswordRequest.getNewPassword(),
                resetPasswordRequest.getConfirmPassword());
        if (result.equals("Password successfully reset ho gaya")) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.badRequest().body(result);
    }
}