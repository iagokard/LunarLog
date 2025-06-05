package com.lunarlog.repository;

import com.lunarlog.model.Shipment;
import com.lunarlog.model.enums.ShipmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

	Optional<Shipment> findByTrackingCode(String trackingCode);

	List<Shipment> findByCurrentStatus(ShipmentStatus status);

	List<Shipment> findBySenderId(Long senderId);

	List<Shipment> findByReceiverId(Long receiverId);

	List<Shipment> findByDeliveryPersonId(Long employeeId);

	List<Shipment> findByIsDeletedFalse();

	@Query("SELECT s FROM Shipment s WHERE s.shipmentDate BETWEEN :startDate AND :endDate")
	List<Shipment> findByShipmentDateBetween(OffsetDateTime startDate, OffsetDateTime endDate);

	@Query("SELECT s FROM Shipment s WHERE s.originLocation.city = :city OR s.destinationLocation.city = :city")
	List<Shipment> findByCity(String city);

	@Modifying
	@Query("UPDATE Shipment s SET s.isDeleted = true WHERE s.id = :id")
	void softDelete(Long id);

	@Modifying
	@Query("UPDATE Shipment s SET s.currentStatus = :status WHERE s.id = :id")
	void updateStatus(Long id, ShipmentStatus status);
}
