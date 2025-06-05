package com.lunarlog.controller;

import com.lunarlog.dto.response.ShipmentResponseDTO;
import com.lunarlog.service.ShipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/envios")
@RequiredArgsConstructor
@Tag(name = "Shipment Management", description = "Endpoints for managing shipments")
public class ShipmentController {

	private final ShipmentService shipmentService;

	@Operation(summary = "Find shipment by tracking code", description = "Retrieves shipment details using its unique tracking code", responses = {
			@ApiResponse(responseCode = "200", description = "Shipment found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ShipmentResponseDTO.class))),
			@ApiResponse(responseCode = "404", description = "Shipment not found")
	})

	@GetMapping("/rastreio/{trackingCode}")
	public ResponseEntity<ShipmentResponseDTO> getShipmentByTrackingCode(
			@Parameter(description = "Tracking code of the shipment to be retrieved", required = true) @PathVariable String trackingCode) {

		ShipmentResponseDTO shipment = shipmentService.findByTrackingCode(trackingCode);
		return ResponseEntity.ok(shipment);
	}
}
