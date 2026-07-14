package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.StudentProfile;

@Repository
public interface StudentProfileRepository extends JpaRepository<StudentProfile,Integer>{
	
    Optional<StudentProfile>findByUserEmail(String email);
    Optional<StudentProfile> findByRollNumber(String rollNumber);
}
