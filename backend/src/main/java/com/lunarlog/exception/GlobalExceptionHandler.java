package com.lunarlog.exception;

import com.lunarlog.dto.response.ExceptionResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ExceptionResponseDTO> handleResourceNotFound(
			ResourceNotFoundException ex, WebRequest request) {

		ExceptionResponseDTO response = new ExceptionResponseDTO(
				OffsetDateTime.now(),
				HttpStatus.NOT_FOUND.value(),
				"Resource Not Found",
				ex.getMessage(),
				request.getDescription(false).replace("uri=", ""));

		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
}
