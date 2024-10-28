package com.example.service.user;

import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.example.domain.enums.UserRole;
import com.example.domain.mapper.UserMapper;
import com.example.domain.model.User;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	
	public final static String NULL_ARGUMENT_MESSAGE = "null cannot be allowed";
	private final UserMapper userMapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Assert.notNull(username, CustomUserDetailsService.NULL_ARGUMENT_MESSAGE);
		User user = userMapper.findUserCredentialsByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException(null));

		GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + UserRole.fromCode(user.getRole()).getRoleName());
		
		return new CustomUserDetails(user.getUserId(), username, user.getPassword(),
				Collections.singletonList(authority));
	}
}
