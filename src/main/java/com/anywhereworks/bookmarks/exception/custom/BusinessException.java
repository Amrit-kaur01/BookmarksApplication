package com.anywhereworks.bookmarks.exception.custom;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

public class BusinessException extends Exception {
	private static final long serialVersionUID = 1L;
	private HttpStatus httpStatus;
	private String message;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyy hh:mm:ss")
	private LocalDateTime timestamp;

	public BusinessException() {
		timestamp = LocalDateTime.now();
	}

	public BusinessException(HttpStatus httpStatus, String message) {
		this();
		this.httpStatus = httpStatus;
		this.message = message;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}
}
