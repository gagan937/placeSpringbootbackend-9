package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.demo.entity.PasswordResetToken;
import com.example.demo.entity.User;
import com.example.demo.repository.PasswordResetTokenRepository;
import com.example.demo.repository.StudentRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
@Service
public class PasswordResetService {
           
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private JavaMailSender mailSender;
	
	
	public String sendOtp(String email) {
		
		       User user=studentRepository.findByEmail(email);
		       if(user == null) {
		    	   throw new RuntimeException("No account found with this email");
		       }
		       
		       String otp=String.valueOf(new Random().nextInt(900000)+100000);
		       
		       PasswordResetToken token=passwordResetTokenRepository.findByEmail(email)
		    		   .orElse(new PasswordResetToken());
		       
		       token.setEmail(email);
		        token.setOtp(otp);
		        token.setExpiryTime(LocalDateTime.now().plusMinutes(5));
		        token.setVerified(false);
		        passwordResetTokenRepository.save(token);
		      
		        
		        SimpleMailMessage message= new SimpleMailMessage();
		        message.setTo(email);
		        message.setSubject("CareerSync - Password Reset OTP");
		        message.setText("Your OTP for password reset is: " + otp + "\nThis OTP is valid for 5 minutes.");
		        
		        
		        mailSender.send(message);
		        
		
		   return "Otp send to your email";
	}
	
	public String verifyOtp(String otp) {
		
		PasswordResetToken verifyotp=passwordResetTokenRepository.findByOtp(otp)
				.orElseThrow(() -> new RuntimeException("Invalid OTP"));
		
		  if(verifyotp.getExpiryTime().isBefore(LocalDateTime.now())) {
			  
			  throw new RuntimeException("OTP expired, please request a new one");
		  }
		  verifyotp.setVerified(true);
		  passwordResetTokenRepository.save(verifyotp);
		  System.out.println("OTP verified successfully");
		return "OTP verified successfully";
	}
	
	public String resetPassword(String otp,String newPassword,String confirmPassword) {
		
		      if(!newPassword.equals(confirmPassword)) {
		    	  
		    	  throw new RuntimeException("Password do not match");
		      }
		      
		     PasswordResetToken resetToken=passwordResetTokenRepository.findByOtp(otp).
		    		 orElseThrow(()-> new RuntimeException("Invalid OTP"));
		     
		     if(!resetToken.isVerified()) {
		    	   throw new RuntimeException("OTP not verified yet. Please verify OTP first");
		     }
		     
		     if(resetToken.getExpiryTime().isBefore(LocalDateTime.now())) {
		    	 throw new RuntimeException("OTP expired, please request a new one");
		     }
		     
		     User user = studentRepository.findByEmail(resetToken.getEmail());
		        
		     if(user ==null) {
		    	 
		    	 throw new RuntimeException("User not found with this email");
		     }
		
		     user.setPassword(passwordEncoder.encode(newPassword));
		        studentRepository.save(user);
		        passwordResetTokenRepository.delete(resetToken);

		return "Password reset successfully";
	}
	
	
	
		
}
