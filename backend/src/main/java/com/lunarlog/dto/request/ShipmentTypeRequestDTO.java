package com.lunarlog.dto.request;

import jakarta.validation.constraints.*;

public record ShipmentTypeRequestDTO(
		@NotBlank @Size(max = 50) String name,
		@NotNull @Positive Integer freightPerWeight,
		@NotNull @Positive Integer freightPerDistance,
		@NotNull @Positive Integer freightPerVolume,
		@Positive Integer maxWeightExemption,
		@Positive Integer maxDistanceExemption,
		@Positive Integer maxVolumeExemption) {
}
