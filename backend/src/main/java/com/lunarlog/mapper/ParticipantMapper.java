package com.lunarlog.mapper;

import com.lunarlog.dto.request.ParticipantRequestDTO;
import com.lunarlog.model.Location;
import com.lunarlog.model.Participant;

public class ParticipantMapper {
	public static Participant toEntity(ParticipantRequestDTO dto, Location location) {
		return Participant.builder()
				.cpf(dto.cpf())
				.fullName(dto.fullName())
				.email(dto.email())
				.phone(dto.phone())
				.location(location)
				.build();
	}
}
