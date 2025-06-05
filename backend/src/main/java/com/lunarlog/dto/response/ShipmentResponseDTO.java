package com.lunarlog.dto.response;

import com.lunarlog.model.ShipmentStatusHistory;
import com.lunarlog.model.enums.ShipmentStatus;
import java.time.OffsetDateTime;
import java.util.List;

public record ShipmentResponseDTO(
		Long id,
		String trackingCode,
		String keyword,
		Long senderId,
		Long receiverId,
		Long packageId,
		String shipmentType,
		Long originLocationId,
		Long destinationLocationId,
		Integer freightValue,
		Long deliveryPersonId,
		ShipmentStatus currentStatus,
		boolean isDeleted,
		OffsetDateTime shipmentDate,
		OffsetDateTime completionDate,
		OffsetDateTime updatedAt,
		List<ShipmentStatusHistory> statusHistory) {
}
