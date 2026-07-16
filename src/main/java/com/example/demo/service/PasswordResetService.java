package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.StudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PasswordResetService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public String sendOtp(String email) {
        User user = studentRepository.findByEmail(email);
        if (user == null) {
            return "Email registered nahi hai";
        }

        String otp = String.valueOf((int) (Math.random() * 900000) + 100000);
        user.setResetOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
        studentRepository.save(user);

        try {
            emailService.sendOtpEmail(email, otp);
        } catch (Exception e) {
            return "OTP bhejne me problem hui. Baad me try karein.";
        }

        return "OTP bhej diya gaya hai aapke email par";
    }

    public String verifyOtp(String otp) {
        User user = studentRepository.findByResetOtp(otp);

        if (user == null) {
            return "Invalid OTP";
        }

        if (user.getOtpExpiry() == null || user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            return "OTP expire ho gaya hai";
        }

        return "OTP verified successfully";
    }

    public String resetPassword(String otp, String newPassword, String confirmPassword) {
        User user = studentRepository.findByResetOtp(otp);

        if (user == null) {
            return "Invalid OTP";
        }

        if (user.getOtpExpiry() == null || user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            return "OTP expire ho gaya hai";
        }

        if (!newPassword.equals(confirmPassword)) {
            return "New password aur confirm password match nahi kar rahe";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetOtp(null);
        user.setOtpExpiry(null);
        studentRepository.save(user);

        return "Password successfully reset ho gaya";
    }
}