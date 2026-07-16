//package com.example.demo.service;
//
//import com.example.demo.entity.User;
//import com.example.demo.repository.StudentRepository;
//
//import java.util.UUID;
//import java.util.regex.Pattern;
//
//import javax.naming.directory.Attribute;
//import javax.naming.directory.Attributes;
//import javax.naming.directory.DirContext;
//import javax.naming.directory.InitialDirContext;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//public class UserService {
//
//    @Autowired
//    private StudentRepository studentRepository;
//
//    @Autowired
//    private BCryptPasswordEncoder passwordEncoder;
//
//   @Autowired
//   private JavaMailSender mailSender;
//   
//   private static final String FRONTEND_VERIFY_URL = "http://localhost:3000/auth/register?token=";
//   private static final String EMAIL_REGEX =
//	        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*(?:\\.[a-zA-Z]{2,})$";
//   
//   
//   private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
//   
//   
//   
//  
//    public User registerUser(User user) {
//    	
//    	String email=user.getEmail();
//    	
//    	 // 1. Format check
//    	if(email==null || !EMAIL_PATTERN.matcher(email).matches()) {
//    		   throw new IllegalArgumentException("Invalid email format");
//    	}
//    	
//    	 // 2. Domain existence check (MX record)
//    	
//    	 if (!isDomainValid(email)) {
//             throw new IllegalArgumentException("Email domain does not exist. Please use a real email address.");
//         }
//    	 // 3. Duplicate check
//    	 
//    	 
//    	 if(studentRepository.findByEmail(email)!=null) {
//    		       
//    		 throw new IllegalArgumentException("Email already registered");
//    		 
//    	 }
//    	//hash code
//        String hashed = passwordEncoder.encode(user.getPassword());
//        user.setPassword(hashed);
//        
//        
//        //5. Account inactive rakho jab tak verify na 
//        user.setEnabled(false);
//        String token = UUID.randomUUID().toString();
//        user.setVerificationToken(token);
//        
//        // 6. Verification email bhejo
//        sendVerificationEmail(user.getEmail(), token);
//        
//        return studentRepository.save(user);
//    }
//
//    public boolean loginUser(String email, String plainPassword) {
//        User user = studentRepository.findByEmail(email);
//
//        if (user == null) return false;
//
//        if (!user.isEnabled()) {
//            return false; // account verify nahi hua
//        }
//
//        return passwordEncoder.matches(plainPassword, user.getPassword());
//    }
//    
//    public 	boolean verifyEmail(String token) {
//    	 User user = studentRepository.findByVerificationToken(token);
//         if (user == null) return false;
//
//         user.setEnabled(true);
//         user.setVerificationToken(null);
//         studentRepository.save(user);
//    	  
//    	   return true;
//    }
//    
//    public User findByEmail(String email) {
//        return studentRepository.findByEmail(email);
//    }
//    
//    
//    
//    // ================= HELPERS =================  
//    
//    
//    private boolean isDomainValid(String email) {
//        try {
//            String domain = email.substring(email.indexOf("@") + 1);
//            DirContext ctx = new InitialDirContext();
//            Attributes attrs = ctx.getAttributes("dns:/" + domain, new String[]{"MX"});
//            Attribute attr = attrs.get("MX");
//            return attr != null && attr.size() > 0;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//    
//    
//    private void sendVerificationEmail(String toEmail, String token) {
//        String link = FRONTEND_VERIFY_URL + token;
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(toEmail);
//        message.setSubject("Verify your email");
//        message.setText("Click the link to verify your account:\n" + link);
//        mailSender.send(message);
//    }
//    
//}




package com.example.demo.service;

import com.example.demo.entity.PendingUser;
import com.example.demo.entity.User;
import com.example.demo.repository.PendingUserRepository;
import com.example.demo.repository.StudentRepository;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PendingUserRepository pendingUserRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Value("${frontend.verify-url}")
    private String FRONTEND_VERIFY_URL;

    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*(?:\\.[a-zA-Z]{2,})$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    // ================= REGISTRATION =================

    public void registerUser(User user) {

        String email = user.getEmail();

        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (!isDomainValid(email)) {
            throw new IllegalArgumentException("Email domain does not exist. Please use a real email address.");
        }

        if (studentRepository.findByEmail(email) != null) {
            throw new IllegalArgumentException("Email already registered");
        }

        if (pendingUserRepository.findByEmail(email) != null) {
            throw new IllegalArgumentException("Verification pending hai. Apna inbox check karein.");
        }

        String hashed = passwordEncoder.encode(user.getPassword());
        String token = UUID.randomUUID().toString();

        PendingUser pending = new PendingUser();
        pending.setUserId(user.getUserId());
        pending.setUsername(user.getUsername());
        pending.setEmail(email);
        pending.setPassword(hashed);
        pending.setPhoneNumber(user.getPhoneNumber());
        pending.setRole(user.getRole());
        pending.setVerificationToken(token);

        String link = FRONTEND_VERIFY_URL + token;

        emailService.sendVerificationEmail(email, link);

        pendingUserRepository.save(pending);
    }

    public boolean verifyEmail(String token) {
        PendingUser pending = pendingUserRepository.findByVerificationToken(token);
        if (pending == null) return false;

        User user = new User();
        user.setUserId(pending.getUserId());
        user.setUsername(pending.getUsername());
        user.setEmail(pending.getEmail());
        user.setPassword(pending.getPassword());
        user.setPhoneNumber(pending.getPhoneNumber());
        user.setRole(pending.getRole());
        user.setEnabled(true);

        studentRepository.save(user);
        pendingUserRepository.delete(pending);

        return true;
    }

    // ================= LOGIN =================

    public boolean loginUser(String email, String plainPassword) {
        User user = studentRepository.findByEmail(email);
        if (user == null) return false;
        if (!user.isEnabled()) return false;
        return passwordEncoder.matches(plainPassword, user.getPassword());
    }

    public User findByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    // ================= FORGOT PASSWORD =================

    public void forgotPassword(String email) {
        User user = studentRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("Email registered nahi hai");
        }

        String otp = String.valueOf((int) (Math.random() * 900000) + 100000);
        user.setResetOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
        studentRepository.save(user);

        emailService.sendOtpEmail(email, otp);
    }

    public boolean verifyOtpAndResetPassword(String email, String otp, String newPassword) {
        User user = studentRepository.findByEmail(email);
        if (user == null) return false;

        if (user.getResetOtp() == null || !user.getResetOtp().equals(otp)) return false;
        if (user.getOtpExpiry() == null || user.getOtpExpiry().isBefore(LocalDateTime.now())) return false;

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetOtp(null);
        user.setOtpExpiry(null);
        studentRepository.save(user);

        return true;
    }

    // ================= HELPERS =================

    private boolean isDomainValid(String email) {
        try {
            String domain = email.substring(email.indexOf("@") + 1);
            DirContext ctx = new InitialDirContext();
            Attributes attrs = ctx.getAttributes("dns:/" + domain, new String[]{"MX"});
            Attribute attr = attrs.get("MX");
            return attr != null && attr.size() > 0;
        } catch (Exception e) {
            return false;
        }
    }
}