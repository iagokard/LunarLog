package com.lunarlog.dto.response;

public record AuthResponseDTO(
		String accessToken,
		Long employeeId,
		String role,
		String fullName) {
}
