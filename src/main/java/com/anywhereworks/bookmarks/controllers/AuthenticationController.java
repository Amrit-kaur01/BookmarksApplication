package com.anywhereworks.bookmarks.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.anywhereworks.bookmarks.entities.User;
import com.anywhereworks.bookmarks.exception.custom.BusinessException;
import com.anywhereworks.bookmarks.models.AuthenticationResponse;
import com.anywhereworks.bookmarks.models.UserCredentials;
import com.anywhereworks.bookmarks.models.UserResponse;
import com.anywhereworks.bookmarks.services.AuthenticationService;

@RestController
public class AuthenticationController {

	@Autowired
	private AuthenticationService authenticationService;

	@PostMapping("/register")
	public ResponseEntity<UserResponse> register(@RequestBody UserCredentials userCredentials) throws BusinessException {
		User user = authenticationService.register(userCredentials);
		UserResponse userResponse = UserResponse.builder().username(user.getUsername()).id(user.getId()).build();
		return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
	}

	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody UserCredentials userCredentials)
			throws BusinessException {
		String jwtToken = authenticationService.authenticate(userCredentials);
		AuthenticationResponse authenticationResponse = AuthenticationResponse.builder().token(jwtToken).build();
		return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
	}
}
