package com.lunarlog.dto.response;

import com.lunarlog.model.enums.EmployeeRole;
import java.time.OffsetDateTime;

public record EmployeeResponseDTO(
		Long id,
		String cpf,
		String fullName,
		String email,
		EmployeeRole role,
		boolean isDeleted,
		OffsetDateTime createdAt,
		OffsetDateTime updatedAt) {
}
