package com.lunarlog.service.impl;

import com.lunarlog.dto.response.ShipmentTypeListResponseDTO;
import com.lunarlog.dto.response.ShipmentTypeResponseDTO;
import com.lunarlog.exception.ResourceNotFoundException;
import com.lunarlog.mapper.ShipmentTypeMapper;
import com.lunarlog.model.ShipmentType;
import com.lunarlog.repository.ShipmentTypeRepository;
import com.lunarlog.service.ShipmentTypeService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShipmentTypeServiceImpl implements ShipmentTypeService {

	private final ShipmentTypeRepository shipmentTypeRepository;

	@Override
	public ShipmentTypeListResponseDTO getAll() {
		List<ShipmentType> shipmentTypes = shipmentTypeRepository.findByIsDeletedFalse();
		return new ShipmentTypeListResponseDTO(shipmentTypes);
	}

	@Override
	public ShipmentTypeResponseDTO findByName(String name) {
		ShipmentType shipmentType = findEntityByName(name);
		return ShipmentTypeMapper.toDTO(shipmentType);
	}

	@Override
	public ShipmentType findEntityByName(String name) {
		return shipmentTypeRepository.findByName(name)
				.orElseThrow(
						() -> new ResourceNotFoundException("Shipment type not found with name: " + name));
	}
}
