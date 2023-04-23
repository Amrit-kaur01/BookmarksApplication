package com.anywhereworks.bookmarks.services;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;

public interface JwtService {
	
	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);
	
	public String generateToken(UserDetails userDetails);

	public String extractUsername(String jwtToken);
	
	public Claims extractAllClaims(String jwtToken);
	
	public <T> T extractClaim (String jwtToken, Function<Claims, T> claimsResolver);
	
	public boolean isTokenValid(String jwtToken, UserDetails userDetails);
	
	public boolean isTokenExpired(String jwtToken);
	
	public Date extractExpiration(String jwtToken);
}
