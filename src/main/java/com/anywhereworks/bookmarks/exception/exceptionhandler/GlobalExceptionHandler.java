package com.anywhereworks.bookmarks.exception.exceptionhandler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.anywhereworks.bookmarks.exception.custom.BusinessException;
import com.anywhereworks.bookmarks.exception.custom.ErrorResponse;

import jakarta.persistence.OptimisticLockException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = BusinessException.class)
	public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException businessException) {
		ErrorResponse errorResponse = new ErrorResponse(businessException.getHttpStatus(),
				businessException.getMessage(), businessException.getTimestamp());
		return new ResponseEntity<>(errorResponse, businessException.getHttpStatus());
	}

	@ExceptionHandler(value = OptimisticLockException.class)
	public ResponseEntity<ErrorResponse> handleOptimisticLockException(BusinessException optimisticLockException) {
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT, optimisticLockException.getMessage(),
				LocalDateTime.now());
		return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
	}

}
