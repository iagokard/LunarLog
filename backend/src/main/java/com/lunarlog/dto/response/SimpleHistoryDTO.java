package com.lunarlog.dto.response;

import com.lunarlog.model.Location;
import com.lunarlog.model.enums.ShipmentStatus;
import java.time.OffsetDateTime;

public record SimpleHistoryDTO(
		Long id,
		ShipmentStatus status,
		Location location,
		Location nextLocation,
		OffsetDateTime eventTime) {
}
