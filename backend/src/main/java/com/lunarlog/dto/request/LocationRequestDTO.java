package com.lunarlog.dto.request;

import jakarta.validation.constraints.*;

public record LocationRequestDTO(
		@NotBlank @Size(min = 8, max = 8) String cep,
		@NotBlank @Size(max = 100) String street,
		@NotBlank @Size(max = 10) String number,
		@Size(max = 100) String complement,
		@NotBlank @Size(max = 100) String neighborhood,
		@NotBlank @Size(max = 130) String city,
		@NotBlank @Size(min = 2, max = 2) String state) {
}
