//
//
//package com.example.demo.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import com.example.demo.entity.User;
//import com.example.demo.repository.StudentRepository;
//import com.example.demo.service.UserService;
//
//@RestController
//@CrossOrigin(origins = "${cors.allowed-origin}")
//public class StudentController {
//
//    @Autowired
//    private StudentRepository studentRepository;
//
//    @Autowired
//    private UserService userService;
//
//    @PostMapping("/users")
//    public ResponseEntity<?> saveUsers(@RequestBody User user) {
//        try {
//            userService.registerUser(user);
//            return ResponseEntity.ok("Registered successfully. Please verify your email.");
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<?> loginUser(@RequestBody User user) {
//        User existingUser = userService.findByEmail(user.getEmail());
//
//        if (existingUser == null) {
//            return ResponseEntity.status(401).body("Invalid email or password");
//        }
//
//        if (!existingUser.isEnabled()) {
//            return ResponseEntity.status(403).body("Please verify your email before logging in");
//        }
//
//        if (userService.loginUser(user.getEmail(), user.getPassword())) {
//            existingUser.setPassword(null);
//            return ResponseEntity.ok(existingUser);
//        } else {
//            return ResponseEntity.status(401).body("Invalid email or password");
//        }
//    }
//
//    @GetMapping("/verify")
//    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
//        boolean verified = userService.verifyEmail(token);
//        if (verified) {
//            return ResponseEntity.ok("Email verified successfully!");
//        }
//        return ResponseEntity.badRequest().body("Invalid or expired token");
//    }
//
//    @GetMapping("/getStudent/{userId}")
//    public ResponseEntity<?> getStudentByUserId(@PathVariable String userId) {
//        User student = studentRepository.findByUserId(userId);
//        if (student == null) {
//            return ResponseEntity.status(404).body("Student not found");
//        }
//        student.setPassword(null);
//        return ResponseEntity.ok(student);
//    }
//}








package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.User;
import com.example.demo.repository.StudentRepository;
import com.example.demo.service.UserService;

@RestController
@CrossOrigin(origins = "${cors.allowed-origin}")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserService userService;

    @PostMapping("/users")
    public ResponseEntity<?> saveUsers(@RequestBody User user) {
        try {
            userService.registerUser(user);
            return ResponseEntity.ok("Verification email bhej diya gaya hai. Please apna inbox check karein.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Email bhejne me problem hui. Baad me try karein.");
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        boolean verified = userService.verifyEmail(token);
        if (verified) {
            return ResponseEntity.ok("Email verified successfully!");
        }
        return ResponseEntity.badRequest().body("Invalid or expired token");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        User existingUser = userService.findByEmail(user.getEmail());

        if (existingUser == null) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }

        if (!existingUser.isEnabled()) {
            return ResponseEntity.status(403).body("Please verify your email before logging in");
        }

        if (userService.loginUser(user.getEmail(), user.getPassword())) {
            existingUser.setPassword(null);
            return ResponseEntity.ok(existingUser);
        } else {
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }

  

    @GetMapping("/getStudent/{userId}")
    public ResponseEntity<?> getStudentByUserId(@PathVariable String userId) {
        User student = studentRepository.findByUserId(userId);
        if (student == null) {
            return ResponseEntity.status(404).body("Student not found");
        }
        student.setPassword(null);
        return ResponseEntity.ok(student);
    }
}