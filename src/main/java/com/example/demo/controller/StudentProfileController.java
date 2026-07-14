//package com.example.demo.controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.example.demo.entity.StudentProfile;
//import com.example.demo.repository.StudentProfileRepository;
//
//@RestController
//@CrossOrigin(origins = "http://localhost:3000")
//public class StudentProfileController {
//	
//	@Autowired
//	private StudentProfileRepository studentProfileRepository;
//	
//	@PostMapping("/student/profile")
//	public List<StudentProfile> studentProfile(@RequestBody StudentProfile studentProfile) {
//		studentProfileRepository.save(studentProfile);
//		return studentProfileRepository.findAll();
//	}
//
//}


package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.StudentProfile;
import com.example.demo.service.StudentProfileService;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class StudentProfileController {

	@Autowired
	private StudentProfileService studentProfileService;

	@PostMapping("/student/profile")
	public StudentProfile studentProfile(@RequestBody StudentProfile studentProfile) {
		return studentProfileService.saveOrUpdateProfile(studentProfile);
	}

	// method for upload resume
	@PostMapping(value = "/student/profile/resume", consumes = "multipart/form-data")
	public ResponseEntity<String> uploadResume(
			@RequestParam("rollNumber") String rollNumber,
			@RequestParam("file") MultipartFile file) {
		try {
			String resumeUrl = studentProfileService.uploadResume(rollNumber, file);
			return ResponseEntity.ok(resumeUrl);
		} catch (IOException e) {
			return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

}