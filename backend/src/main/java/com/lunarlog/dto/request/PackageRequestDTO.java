package com.lunarlog.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.*;

public record PackageRequestDTO(
		@NotNull @Positive BigDecimal heightCm,
		@NotNull @Positive BigDecimal widthCm,
		@NotNull @Positive BigDecimal depthCm,
		@NotNull @Positive Integer weightGrams,
		String description) {
}
