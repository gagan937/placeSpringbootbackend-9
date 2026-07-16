package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.PendingUser;

public interface PendingUserRepository extends JpaRepository<PendingUser,Integer>{
	
	PendingUser findByEmail(String email);
	PendingUser findByVerificationToken(String token);

}
