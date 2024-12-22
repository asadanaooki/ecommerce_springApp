package com.example.service.user;

import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.example.domain.mapper.UserMapper;
import com.example.domain.model.entity.User;
import com.example.domain.model.entity.UserExample;
import com.example.domain.model.security.LoginUserDetail;

import lombok.AllArgsConstructor;

/**
 * 認証処理のために UserDetails オブジェクトを構築する
 */
@Service
@AllArgsConstructor
public class LoginService implements UserDetailsService {
	
	/**
	 * null引数を許容しない場合にスローする例外メッセージ
	 */
	public final static String NULL_ARGUMENT_MESSAGE = "null cannot be allowed";
	
	/**
	 * ユーザーマッパー
	 */
	private final UserMapper userMapper;

	/**
	 * ユーザー情報を取得し、UserDetails オブジェクトを返します
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Assert.notNull(username, LoginService.NULL_ARGUMENT_MESSAGE);
		UserExample example = new UserExample();
		example.createCriteria().andEmailEqualTo(username);
		
		List<User> users = userMapper.selectByExample(example);
		User user = users.stream().findFirst()
				.orElseThrow(() -> new UsernameNotFoundException(null));

		GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleName());
		
		return new LoginUserDetail(user.getUserId(), username, user.getPassword(),
				Collections.singletonList(authority));
	}
}
