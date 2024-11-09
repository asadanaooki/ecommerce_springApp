package com.example.domain.enums;

import lombok.Getter;

@Getter
public enum Gender {
	MALE("M"),
	FEMALE("F");
	
	private final String code;
	
	Gender(String code){
		this.code = code;
	}
	
}
