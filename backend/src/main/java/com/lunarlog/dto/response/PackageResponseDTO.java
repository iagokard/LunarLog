package com.lunarlog.dto.response;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record PackageResponseDTO(
		Long id,
		BigDecimal heightCm,
		BigDecimal widthCm,
		BigDecimal depthCm,
		Integer weightGrams,
		Integer volumeCm3,
		String description,
		boolean isDeleted,
		OffsetDateTime createdAt,
		OffsetDateTime updatedAt) {
}
