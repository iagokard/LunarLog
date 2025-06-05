package com.lunarlog.repository;

import com.lunarlog.model.Shipment;
import com.lunarlog.model.ShipmentStatusHistory;
import com.lunarlog.model.enums.ShipmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface ShipmentStatusHistoryRepository extends JpaRepository<ShipmentStatusHistory, Long> {

    @Query("SELECT h FROM ShipmentStatusHistory h WHERE h.shipmentId = :shipmentId ORDER BY h.eventTime DESC")
	List<ShipmentStatusHistory> findByShipmentId(Long shipmentId);

	List<ShipmentStatusHistory> findByStatus(ShipmentStatus status);

	List<ShipmentStatusHistory> findByLocationId(Long locationId);

	// @Query("SELECT h FROM ShipmentStatusHistory h WHERE h.eventTime BETWEEN
	// :start AND :end")
	// List<ShipmentStatusHistory> findByEventTimeBetween(OffsetDateTime start,
	// OffsetDateTime end);
	//
	// @Query("SELECT h FROM ShipmentStatusHistory h WHERE h.shipment.id =
	// :shipmentId ORDER BY h.eventTime DESC")
	// List<ShipmentStatusHistory> findHistoryByShipmentOrdered(Long shipmentId);
}
