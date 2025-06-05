package com.lunarlog.dto.request;

import jakarta.validation.constraints.*;

public record ShipmentRequestDTO(
		@NotBlank @Size(max = 20) String trackingCode,
		@NotBlank @Size(max = 50) String keyword,
		@NotNull Long senderId,
		@NotNull Long receiverId,
		@NotNull Long packageId,
		@NotNull Long shipmentTypeId,
		@NotNull Long originLocationId,
		@NotNull Long destinationLocationId,
		@NotNull @Positive Integer freightValue,
		Long deliveryPersonId) {
}
