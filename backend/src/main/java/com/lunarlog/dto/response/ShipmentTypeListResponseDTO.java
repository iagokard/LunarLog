package com.lunarlog.dto.response;

import java.util.List;

import com.lunarlog.model.ShipmentType;

public record ShipmentTypeListResponseDTO(List<ShipmentType> shipmentTypes) {
}
