package com.lunarlog.dto.response;

import com.lunarlog.model.enums.ShipmentStatus;
import java.time.OffsetDateTime;

public record ShipmentStatusResponseDTO(
		Long id,
		Long shipmentId,
		ShipmentStatus status,
		Long locationId,
		Long nextLocationId,
		OffsetDateTime eventTime) {
}
