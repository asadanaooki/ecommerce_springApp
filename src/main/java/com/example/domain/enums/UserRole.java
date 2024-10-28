package com.example.domain.enums;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;

import com.example.service.user.CustomUserDetailsService;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserRole {
	USER("1", "USER"), MANAGER("2", "MANAGER");

	private final String code;
	private final String roleName;

	private static final Map<String, UserRole> codeToRoleMap = new HashMap<String, UserRole>();

	static {
		for (UserRole role : UserRole.values()) {
			codeToRoleMap.put(role.code, role);
		}
	}
	
	public static UserRole fromCode(String code) {
		Assert.notNull(code, CustomUserDetailsService.NULL_ARGUMENT_MESSAGE);
		return codeToRoleMap.get(code);
	}
}
