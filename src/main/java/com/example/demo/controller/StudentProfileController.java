

package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.StudentProfile;
import com.example.demo.service.StudentProfileService;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "${cors.allowed-origin}")
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