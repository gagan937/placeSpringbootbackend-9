//package com.example.demo.service;
//
//import java.io.IOException;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.example.demo.entity.StudentProfile;
//import com.example.demo.entity.User;
//import com.example.demo.repository.StudentProfileRepository;
//import com.example.demo.repository.StudentRepository;
//
//@Service
//public class StudentProfileService {
//
//    @Autowired
//    private StudentProfileRepository studentProfileRepository;
//
//    @Autowired
//    private StudentRepository studentRepository;
//
//    public StudentProfile saveOrUpdateProfile(StudentProfile incoming) {
//
//      
//        StudentProfile profile = studentProfileRepository
//                .findByRollNumber(incoming.getRollNumber())
//                .orElse(new StudentProfile());
//
//        
//        if (profile.getUser() == null) {
//            User user = studentRepository.findByUserId(incoming.getRollNumber());
//
//            if (user == null) {
//                throw new RuntimeException("User not found with userId: " + incoming.getRollNumber());
//            }
//
//            profile.setUser(user);
//        }
//
//        profile.setRollNumber(incoming.getRollNumber());
//        profile.setBranch(incoming.getBranch());
//        profile.setCgpa(incoming.getCgpa());
//        profile.setActiveBacklogs(incoming.getActiveBacklogs());
//
//        return studentProfileRepository.save(profile);
//    }
//    
//  //method for resume upload
//    
//    public String uploadResume(String rollNumber,MultipartFile file) throws IOException{
//         
//    	   StudentProfile profile=studentProfileRepository;
//    	return "resume upload";
//    }
//}





package com.example.demo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.StudentProfile;
import com.example.demo.entity.User;
import com.example.demo.repository.StudentProfileRepository;
import com.example.demo.repository.StudentRepository;

@Service
public class StudentProfileService {

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    @Autowired
    private StudentRepository studentRepository;

    public StudentProfile saveOrUpdateProfile(StudentProfile incoming) {

        StudentProfile profile = studentProfileRepository
                .findByRollNumber(incoming.getRollNumber())
                .orElse(new StudentProfile());

        if (profile.getUser() == null) {
            User user = studentRepository.findByUserId(incoming.getRollNumber());

            if (user == null) {
                throw new RuntimeException("User not found with userId: " + incoming.getRollNumber());
            }

            profile.setUser(user);
        }

        profile.setRollNumber(incoming.getRollNumber());
        profile.setBranch(incoming.getBranch());
        profile.setCgpa(incoming.getCgpa());
        profile.setActiveBacklogs(incoming.getActiveBacklogs());

        return studentProfileRepository.save(profile);
    }

    // Method for resume upload
    public String uploadResume(String rollNumber, MultipartFile file) throws IOException {

        StudentProfile profile = studentProfileRepository
                .findByRollNumber(rollNumber)
                .orElseThrow(() -> new RuntimeException("Profile not found for roll number: " + rollNumber));

        if (!"application/pdf".equals(file.getContentType())) {
            throw new RuntimeException("Only PDF files are allowed");
        }

        String uploadDir = "uploads/resumes/";
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = rollNumber + "_resume.pdf";
        Path filePath = uploadPath.resolve(fileName);
        Files.write(filePath, file.getBytes());

        String resumeUrl = "/uploads/resumes/" + fileName;
        profile.setResumeUrl(resumeUrl);
        studentProfileRepository.save(profile);
        
        return resumeUrl;
    }
}