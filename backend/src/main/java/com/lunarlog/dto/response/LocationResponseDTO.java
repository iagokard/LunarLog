package com.lunarlog.dto.response;

import java.time.OffsetDateTime;

public record LocationResponseDTO(
		Long id,
		String cep,
		String street,
		String number,
		String complement,
		String neighborhood,
		String city,
		String state,
		boolean isDeleted,
		OffsetDateTime createdAt,
		OffsetDateTime updatedAt) {
}
