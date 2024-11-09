package com.example.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserRole {
	USER("1", "ROLE_USER"), MANAGER("2", "ROLE_MANAGER");

	private final String code;
	private final String roleName;
}
