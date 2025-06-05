package com.lunarlog.dto.response;

import java.time.OffsetDateTime;

public record ParticipantResponseDTO(
		Long id,
		String cpf,
		String fullName,
		String email,
		String phone,
		Long locationId,
		boolean isDeleted,
		OffsetDateTime createdAt,
		OffsetDateTime updatedAt) {
}
