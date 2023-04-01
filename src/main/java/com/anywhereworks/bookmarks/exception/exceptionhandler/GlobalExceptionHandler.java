package com.anywhereworks.bookmarks.exception.exceptionhandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.anywhereworks.bookmarks.exception.custom.BusinessException;
import com.anywhereworks.bookmarks.exception.custom.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = BusinessException.class)
	public ResponseEntity<Object> handleBusinessException(BusinessException businessException) {
		ErrorResponse errorResponse = new ErrorResponse(businessException.getHttpStatus(),
				businessException.getMessage(), businessException.getTimestamp());
		return new ResponseEntity(errorResponse, businessException.getHttpStatus());
	}
}
