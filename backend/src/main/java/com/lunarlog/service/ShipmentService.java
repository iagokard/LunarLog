package com.lunarlog.service;

import com.lunarlog.dto.request.ShipmentRequestDTO;
import com.lunarlog.dto.response.ShipmentResponseDTO;
import com.lunarlog.model.Shipment;
import com.lunarlog.model.ShipmentStatusHistory;
import java.util.List;

public interface ShipmentService {
	ShipmentResponseDTO findByTrackingCode(String trackingCode);

	Shipment findEntityByTrackingCode(String trackingCode);

	List<ShipmentStatusHistory> findHistoryEntityByShipmentId(Long shipmentId);

	ShipmentResponseDTO createShipment(ShipmentRequestDTO dto);
}
