package com.lunarlog.dto.response;

import java.time.OffsetDateTime;

public record ExceptionResponseDTO(
		OffsetDateTime timestamp,
		int status,
		String error,
		String message,
		String path) {
}
