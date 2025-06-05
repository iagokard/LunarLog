package com.lunarlog.dto.request;

import com.lunarlog.model.enums.EmployeeRole;
import jakarta.validation.constraints.*;

public record EmployeeRequestDTO(
		@NotBlank @Size(min = 11, max = 11) String cpf,
		@NotBlank @Size(max = 150) String fullName,
		@NotBlank @Email @Size(max = 250) String email,
		@NotBlank @Size(min = 6) String password,
		@NotNull EmployeeRole role) {
}
