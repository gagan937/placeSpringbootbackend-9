//package com.example.demo.repository;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import com.example.demo.entity.User;
//
//
//@Repository
//public interface StudentRepository extends JpaRepository<User,Integer>{
//
//	 User findByEmail(String email);
//	 User findByUserId(String userId);
//     User findByVerificationToken(String token);
//}



package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    User findByUserId(String userId);
    User findByVerificationToken(String token);
    User findByResetOtp(String otp);   // <-- naya method
}