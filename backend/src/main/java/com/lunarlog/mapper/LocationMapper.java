package com.lunarlog.mapper;

import com.lunarlog.dto.request.LocationRequestDTO;
import com.lunarlog.model.Location;

public class LocationMapper {
	public static Location toEntity(LocationRequestDTO dto) {
		return Location.builder()
				.cep(dto.cep())
				.street(dto.street())
				.number(dto.number())
				.complement(dto.complement())
				.neighborhood(dto.neighborhood())
				.city(dto.city())
				.state(dto.state())
				.build();
	}
}
