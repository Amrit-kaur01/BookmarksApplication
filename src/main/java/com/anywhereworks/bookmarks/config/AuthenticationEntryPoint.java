package com.anywhereworks.bookmarks.config;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {
		response.setStatus(401);
		PrintWriter writer = response.getWriter();
		writer.println("Acces denied due to unauthorized access");
	}

	@Override
	public void afterPropertiesSet() {
		setRealmName("Bookmarks");
		super.afterPropertiesSet();
	}

}
