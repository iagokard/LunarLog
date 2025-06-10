package com.lunarlog.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record ShipmentRequestDTO(
		@NotNull @Valid ParticipantRequestDTO sender,
		@NotNull @Valid ParticipantRequestDTO receiver,

		@NotNull @Valid PackageRequestDTO packageEntity,

		@NotBlank @Valid String shipmentType,

		@NotNull @Valid LocationRequestDTO originLocation,
		@NotNull @Valid LocationRequestDTO destinationLocation,

		@NotNull @Positive Integer freightValue,
		Long deliveryPersonId // Pode ser nulo
) {
}
