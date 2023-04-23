package com.anywhereworks.bookmarks.services;

import com.anywhereworks.bookmarks.entities.User;
import com.anywhereworks.bookmarks.exception.custom.BusinessException;
import com.anywhereworks.bookmarks.models.AuthenticationResponse;
import com.anywhereworks.bookmarks.models.UserCredentials;
import com.anywhereworks.bookmarks.models.UserResponse;

public interface AuthenticationService {

	public UserResponse register(UserCredentials userCredentials);

	public AuthenticationResponse authenticate (UserCredentials userCredentials) throws BusinessException;

}
