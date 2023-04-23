package com.anywhereworks.bookmarks.exception.exceptionhandler;

import java.sql.SQLException;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.anywhereworks.bookmarks.exception.custom.BusinessException;
import com.anywhereworks.bookmarks.exception.custom.ErrorResponse;

import jakarta.persistence.OptimisticLockException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = BusinessException.class)
	public ResponseEntity<Object> handleBusinessException(BusinessException businessException) {
		ErrorResponse errorResponse = new ErrorResponse(businessException.getHttpStatus(),
				businessException.getMessage(), businessException.getTimestamp());
		return new ResponseEntity(errorResponse, businessException.getHttpStatus());
	}
	
	@ExceptionHandler(value = SQLException.class)
	public ResponseEntity<Object> handleSQLException(SQLException sqlException) {
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT,
				sqlException.getMessage(), LocalDateTime.now());
		return new ResponseEntity(errorResponse, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(value = OptimisticLockException.class)
	public ResponseEntity<Object> handleOptimisticLockException(BusinessException optimisticLockException) {
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT,
				optimisticLockException.getMessage(), LocalDateTime.now());
		return new ResponseEntity(errorResponse,HttpStatus.CONFLICT);
	}
	
}
