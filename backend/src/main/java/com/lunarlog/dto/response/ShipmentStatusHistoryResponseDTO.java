package com.lunarlog.dto.response;

import com.lunarlog.model.enums.ShipmentStatus;
import java.time.OffsetDateTime;

public record ShipmentStatusHistoryResponseDTO(
		Long id,
		ShipmentStatus status,
		LocationResponseDTO location,
		LocationResponseDTO nextLocation,
		OffsetDateTime eventTime) {
}
