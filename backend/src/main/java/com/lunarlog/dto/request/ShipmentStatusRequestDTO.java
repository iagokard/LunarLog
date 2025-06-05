package com.lunarlog.dto.request;

import com.lunarlog.model.enums.ShipmentStatus;
import jakarta.validation.constraints.NotNull;

public record ShipmentStatusRequestDTO(
		@NotNull ShipmentStatus status,
		@NotNull Long locationId,
		Long nextLocationId) {
}
