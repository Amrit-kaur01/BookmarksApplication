package com.anywhereworks.bookmarks.servicesImpl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.anywhereworks.bookmarks.services.JwtService;
import com.mysql.cj.x.protobuf.MysqlxExpect.Open.Condition.Key;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl implements JwtService {

	private static final String SECRET_KEY = "423F4528482B4D6251655468576D5A7134743777217A25432A46294A404E6352";

	@Override
	public String extractUsername(String jwtToken) {
		return extractClaim(jwtToken, Claims::getSubject);
	}

	@Override
	public Claims extractAllClaims(String jwtToken) {
		return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(jwtToken).getBody();
	}

	private SecretKey getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	@Override
	public <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(jwtToken);
		return claimsResolver.apply(claims);
	}

	@Override
	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<>(), userDetails);
	}

	@Override
	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 20 * 60 * 1000))
				.signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
	}

	@Override
	public boolean isTokenValid(String jwtToken, UserDetails userDetails) {
		final String username = extractUsername(jwtToken);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken));
	}
	
	@Override
	public boolean isTokenExpired(String jwtToken) {
		return extractExpiration(jwtToken).before(new Date());
	}

	@Override
	public Date extractExpiration(String jwtToken) {
		return extractClaim(jwtToken, Claims::getExpiration);
	}
	
	

}
