package com.lunarlog.dto.response;

import java.time.OffsetDateTime;

public record ShipmentTypeResponseDTO(
		Long id,
		String name,
		Integer freightPerWeight,
		Integer freightPerDistance,
		Integer freightPerVolume,
		Integer maxWeightExemption,
		Integer maxDistanceExemption,
		Integer maxVolumeExemption,
		boolean isDeleted,
		OffsetDateTime createdAt,
		OffsetDateTime updatedAt) {
}
