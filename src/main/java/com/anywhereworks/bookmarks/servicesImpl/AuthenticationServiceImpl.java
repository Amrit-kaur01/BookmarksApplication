package com.anywhereworks.bookmarks.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.anywhereworks.bookmarks.entities.User;
import com.anywhereworks.bookmarks.exception.custom.BusinessException;
import com.anywhereworks.bookmarks.models.AuthenticationResponse;
import com.anywhereworks.bookmarks.models.Role;
import com.anywhereworks.bookmarks.models.UserCredentials;
import com.anywhereworks.bookmarks.models.UserResponse;
import com.anywhereworks.bookmarks.repositories.UserRepository;
import com.anywhereworks.bookmarks.services.AuthenticationService;
import com.anywhereworks.bookmarks.services.JwtService;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public UserResponse register(UserCredentials userCredentials) {
		var user = User.builder().username(userCredentials.getUsername())
				.password(passwordEncoder.encode(userCredentials.getPassword())).role(Role.USER).build();
		userRepository.save(user);
		UserResponse userResponse = UserResponse.builder().username(user.getUsername()).id(user.getId()).build();
		return userResponse;
	}

	@Override
	public AuthenticationResponse authenticate(UserCredentials userCredentials) throws BusinessException {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userCredentials.getUsername(),
					userCredentials.getPassword()));

		} catch (BadCredentialsException exception) {
			throw new BusinessException(HttpStatus.UNAUTHORIZED, "Incorrect username/password");
		}

		var user = userRepository.findByUsername(userCredentials.getUsername()).get();
		String jwtToken = jwtService.generateToken(user);
		return AuthenticationResponse.builder().token(jwtToken).build();
	}

}
