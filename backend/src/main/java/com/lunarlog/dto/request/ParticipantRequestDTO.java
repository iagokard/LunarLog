package com.lunarlog.dto.request;

import jakarta.validation.constraints.*;

public record ParticipantRequestDTO(
		@NotBlank @Size(min = 11, max = 11) String cpf,
		@NotBlank @Size(max = 150) String fullName,
		@NotBlank @Email @Size(max = 250) String email,
		@NotBlank @Size(max = 20) String phone) {
}
