package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="student_profile")
public class StudentProfile {

	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Integer id;

	    @OneToOne
	    @JoinColumn(name="user_id" , referencedColumnName = "id")
	    private User user;
	    
	    private String rollNumber;
	    private String branch;
	    private Double cgpa;
	    private Integer activeBacklogs;
	    private String resumeUrl;
}
