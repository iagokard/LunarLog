package com.lunarlog.mapper;

import com.lunarlog.dto.request.PackageRequestDTO;
import com.lunarlog.model.PackageEntity;

public class PackageMapper {
	public static PackageEntity toEntity(PackageRequestDTO dto) {
		return PackageEntity.builder()
				.heightCm(dto.heightCm())
				.widthCm(dto.widthCm())
				.depthCm(dto.depthCm())
				.weightGrams(dto.weightGrams())
				.description(dto.description())
				.build();
	}
}
