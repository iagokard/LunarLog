package com.lunarlog.service;

import com.lunarlog.dto.response.ShipmentTypeResponseDTO;
import com.lunarlog.dto.response.ShipmentTypeListResponseDTO;
import com.lunarlog.model.ShipmentType;

public interface ShipmentTypeService {
	ShipmentTypeListResponseDTO getAll();

	ShipmentTypeResponseDTO findByName(String name);

	ShipmentType findEntityByName(String name);
}
