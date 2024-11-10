package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.AllArgsConstructor;

/**
 * Spring Security の設定を行うクラス
 */
@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

	/**
	 * ユーザー情報を取得するためのサービス
	 */
	private final UserDetailsService userDetailsService;

	/**
	 * H2コンソール用のセキュリティ設定を定義します
	 * 
	 * @param httpSecurity HttpSecurityオブジェクト
	 * @return セキュリティフィルタチェーン
	 * @throws Exception 設定時の例外
	 */
	@Bean
	@Profile("test")
	public SecurityFilterChain h2ConsoleSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.securityMatcher(new AntPathRequestMatcher("/h2-console/**"))
				.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll()).csrf(csrf -> csrf.disable())
				.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

		return httpSecurity.build();
	}

	/**
	 * 通常のアプリケーション用のセキュリティ設定を定義します。
	 * 
	 * @param httpSecurity HttpSecurityオブジェクト
	 * @return セキュリティフィルタチェーン
	 * @throws Exception 設定時の例外
	 */
	@Bean
	@Profile("!test")
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
				.authorizeHttpRequests(authorize -> authorize.requestMatchers("/login", "/user/register").permitAll() // /loginは許可
						.anyRequest().authenticated()) // その他のリクエストは認証を要求
				.formLogin(form -> form.loginPage("/login").usernameParameter("email").failureUrl("/login?error")
						.loginProcessingUrl("/process-login").permitAll());

		return httpSecurity.build();
	}

	/**
	 * AuthenticationManager を定義します。
	 * 
	 * @param httpSecurity HttpSecurityオブジェクト
	 * @return AuthenticationManagerオブジェクト
	 * @throws Exception 設定時の例外
	 */
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
		AuthenticationManagerBuilder auth = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
		auth.userDetailsService(userDetailsService);
		return auth.build();
	}
}
