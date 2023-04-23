package com.anywhereworks.bookmarks.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.anywhereworks.bookmarks.exception.custom.BusinessException;
import com.anywhereworks.bookmarks.repositories.UserRepository;
import com.mysql.cj.protocol.AuthenticationProvider;

import lombok.RequiredArgsConstructor;

@Configuration
public class ApplicationConfiguration {
	
	@Autowired
	private UserRepository userRepository;

	@Bean
	public UserDetailsService userDetailsService() {
		return username -> userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Username not found"));
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}
	
	@Bean
	public AuthenticationManager autheticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
