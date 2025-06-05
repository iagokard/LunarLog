package com.lunarlog.service.impl;

import com.lunarlog.dto.response.ShipmentResponseDTO;
import com.lunarlog.exception.ResourceNotFoundException;
import com.lunarlog.mapper.ShipmentMapper;
import com.lunarlog.model.Shipment;
import com.lunarlog.model.ShipmentStatusHistory;
import com.lunarlog.repository.ShipmentRepository;
import com.lunarlog.repository.ShipmentStatusHistoryRepository;
import com.lunarlog.service.ShipmentService;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {

	private final ShipmentRepository shipmentRepository;
	private final ShipmentStatusHistoryRepository shipmentHistoryRepository;

	@Override
	public ShipmentResponseDTO findByTrackingCode(String trackingCode) {
		Shipment shipment = findEntityByTrackingCode(trackingCode);
		ShipmentResponseDTO dto = ShipmentMapper.toDTO(shipment);

		List<ShipmentStatusHistory> history = findHistoryEntityByShipmentId(shipment.getId());

		return new ShipmentResponseDTO(
				dto.id(),
				dto.trackingCode(),
				dto.keyword(),
				dto.senderId(),
				dto.receiverId(),
				dto.packageId(),
				dto.shipmentType(),
				dto.originLocationId(),
				dto.destinationLocationId(),
				dto.freightValue(),
				dto.deliveryPersonId(),
				dto.currentStatus(),
				dto.isDeleted(),
				dto.shipmentDate(),
				dto.completionDate(),
				dto.updatedAt(),
				history);
	}

	@Override
	public Shipment findEntityByTrackingCode(String trackingCode) {
		return shipmentRepository.findByTrackingCode(trackingCode)
				.orElseThrow(
						() -> new ResourceNotFoundException("Shipment not found with tracking code: " + trackingCode));
	}

	@Override
	public List<ShipmentStatusHistory> findHistoryEntityByShipmentId(Long shipmentId) {
		return shipmentHistoryRepository.findByShipmentId(shipmentId);
	}
}
