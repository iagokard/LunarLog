package com.lunarlog.controller;

import com.lunarlog.dto.response.ShipmentTypeResponseDTO;
import com.lunarlog.service.ShipmentTypeService;
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
public class ShipmentTypeController {

	private final ShipmentTypeService shipmentTypeService;

	@Operation(summary = "Find shipment type by name", description = "Retrieves shipment type details using its unique name", responses = {
			@ApiResponse(responseCode = "200", description = "Shipment type found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ShipmentTypeResponseDTO.class))),
			@ApiResponse(responseCode = "404", description = "Shipment type not found")
	})

	@GetMapping("/tipo/{typeName}")
	public ResponseEntity<ShipmentTypeResponseDTO> getShipmentTypeByName(
			@Parameter(description = "name of the shipment type to be retrieved", required = true) @PathVariable String typeName) {

		ShipmentTypeResponseDTO shipmentType = shipmentTypeService.findByName(typeName);
		return ResponseEntity.ok(shipmentType);
	}
}
