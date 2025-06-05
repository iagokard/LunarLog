package com.lunarlog.service;

import com.lunarlog.dto.response.ShipmentTypeResponseDTO;
import com.lunarlog.model.ShipmentType;

public interface ShipmentTypeService {
	ShipmentTypeResponseDTO findByName(String name);

	ShipmentType findEntityByName(String name);
}
