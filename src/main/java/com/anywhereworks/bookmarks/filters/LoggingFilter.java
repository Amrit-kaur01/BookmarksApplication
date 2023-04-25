package com.anywhereworks.bookmarks.filters;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(1)
public class LoggingFilter extends OncePerRequestFilter {
	private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		ContentCachingRequestWrapper contentCachingRequestWrapper = new ContentCachingRequestWrapper(request);
		ContentCachingResponseWrapper contentCachingResponseWrapper = new ContentCachingResponseWrapper(response);

		filterChain.doFilter(contentCachingRequestWrapper, contentCachingResponseWrapper);

		String requestBody = getStringValue(contentCachingRequestWrapper.getContentAsByteArray(),
				request.getCharacterEncoding());
		logger.info("Received request: Method = {}, Request URI = {}. RequestBody = {}", request.getMethod(),
				request.getRequestURI(), requestBody);

		String responseBody = getStringValue(contentCachingResponseWrapper.getContentAsByteArray(),
				response.getCharacterEncoding());
		logger.info("Sent response: ResponseBody = {} and StatusCode = {}", responseBody, response.getStatus());

		contentCachingResponseWrapper.copyBodyToResponse();
	}

	private String getStringValue(byte[] contentAsByteArray, String characterEncoding)
			throws UnsupportedEncodingException {
		return new String(contentAsByteArray, 0, contentAsByteArray.length, characterEncoding);
	}

}
