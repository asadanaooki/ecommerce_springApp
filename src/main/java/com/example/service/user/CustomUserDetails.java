package com.example.service.user;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

@Getter
public class CustomUserDetails extends User {

	private String email;

	public CustomUserDetails(String userId, String email, String password,
			Collection<? extends GrantedAuthority> authorities) {
		super(userId, password, authorities);
		this.email = email;
	}
}