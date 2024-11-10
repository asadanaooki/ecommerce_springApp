package com.example.service.user;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.domain.model.entity.User;
import com.example.domain.model.enums.UserRole;
import com.example.domain.model.security.CustomUserDetails;
import com.example.domain.mapper.UserMapper;

@SpringBootTest
class CustomUserDetailsServiceTest {

	private final String testUsername = "test@example.com";
	private final String testPassword = "password";
	private final String testRoleName = "ROLE_USER";
	private final String testUserId = UUID.randomUUID().toString();

	@MockBean
	UserMapper userMapper;

	@Autowired
	UserDetailsService userDetailsService;

	@Test
	void loadUserByUsername_UserExist() {
		User user = new User();
		user.setUserId(testUserId);
		user.setEmail(testUsername);
		user.setPassword(testPassword);
		user.setRole(UserRole.USER);

		when(userMapper.findUserCredentialsByEmail(testUsername)).thenReturn(Optional.of(user));

		CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(testUsername);

		assertThat(userDetails.getUsername()).isEqualTo(testUserId);
		assertThat(userDetails.getEmail()).isEqualTo(testUsername);
		assertThat(userDetails.getPassword()).isEqualTo(testPassword);
		assertThat(userDetails.getAuthorities()).singleElement().extracting(GrantedAuthority::getAuthority)
		.isEqualTo(testRoleName);
	}
	
	@Test
	void loadUserByUsername_UserNotExist(){
		when(userMapper.findUserCredentialsByEmail(testUsername)).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> userDetailsService.loadUserByUsername(testUsername))
		.isInstanceOf(UsernameNotFoundException.class);
	}
	
	@Test
	void loadUserByUsername_nullArgment() {
		assertThatThrownBy(() -> userDetailsService.loadUserByUsername(null))
		.isInstanceOf(IllegalArgumentException.class);
	}

}
