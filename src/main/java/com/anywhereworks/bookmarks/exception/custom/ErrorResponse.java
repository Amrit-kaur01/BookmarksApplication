package com.anywhereworks.bookmarks.exception.custom;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ErrorResponse {
	private HttpStatus httpStatus;
	private String message;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyy hh:mm:ss")
	private LocalDateTime timestamp;

	public ErrorResponse(HttpStatus httpStatus, String message, LocalDateTime timestamp) {

		this.setHttpStatus(httpStatus);
		this.setMessage(message);
		this.timestamp = timestamp;
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
}
