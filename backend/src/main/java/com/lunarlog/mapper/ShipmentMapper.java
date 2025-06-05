package com.lunarlog.mapper;

import java.util.List;

import com.lunarlog.dto.response.ShipmentResponseDTO;
import com.lunarlog.model.Shipment;
import com.lunarlog.model.ShipmentStatusHistory;

public class ShipmentMapper {

	public static ShipmentResponseDTO toDTO(Shipment shipment) {
		return new ShipmentResponseDTO(
				shipment.getId(),
				shipment.getTrackingCode(),
				shipment.getKeyword(),
				shipment.getSender().getId(),
				shipment.getReceiver().getId(),
				shipment.getPackageEntity().getId(),
				shipment.getShipmentType().getName(),
				shipment.getOriginLocation().getId(),
				shipment.getDestinationLocation().getId(),
				shipment.getFreightValue(),
				shipment.getDeliveryPerson() != null ? shipment.getDeliveryPerson().getId() : null,
				shipment.getCurrentStatus(),
				shipment.isDeleted(),
				shipment.getShipmentDate(),
				shipment.getCompletionDate(),
				shipment.getUpdatedAt(),
				null);

	}
}
