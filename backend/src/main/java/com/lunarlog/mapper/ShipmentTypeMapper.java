package com.lunarlog.mapper;

import com.lunarlog.dto.response.ShipmentTypeResponseDTO;
import com.lunarlog.model.ShipmentType;

public class ShipmentTypeMapper {

	public static ShipmentTypeResponseDTO toDTO(ShipmentType shipmentType) {
		return new ShipmentTypeResponseDTO(
				shipmentType.getId(),
				shipmentType.getName(),
				shipmentType.getFreightPerWeight(),
				shipmentType.getFreightPerDistance(),
				shipmentType.getFreightPerVolume(),
				shipmentType.getMaxWeightExemption(),
				shipmentType.getMaxDistanceExemption(),
				shipmentType.getMaxVolumeExemption(),
				shipmentType.isDeleted(),
				shipmentType.getCreatedAt(),
				shipmentType.getUpdatedAt());
	}
}
