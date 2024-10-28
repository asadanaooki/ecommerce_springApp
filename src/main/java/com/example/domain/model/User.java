package com.example.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

	private String userId;
	
	private String email;
	
	private String password;
	
	private String firstNameKanji;
	
	private String lastNameKanji;
	
	private String firstNameKana;
	
	private String lastNameKana;
	
	private String gender; // 'M' or 'F'
	
	private LocalDate birthDate;
	
	private String postCode;
	
	private String address;
	
	private String phoneNumber;
	
	private String role;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;
}