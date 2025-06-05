package com.lunarlog.model;

import com.lunarlog.model.enums.ShipmentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "shipments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shipment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "tracking_code", nullable = false, unique = true, length = 20)
	private String trackingCode;

	@Column(nullable = false, length = 50)
	private String keyword;

	@ManyToOne
	@JoinColumn(name = "sender_id", nullable = false)
	private Participant sender;

	@ManyToOne
	@JoinColumn(name = "receiver_id", nullable = false)
	private Participant receiver;

	@ManyToOne
	@JoinColumn(name = "package_id", nullable = false)
	private PackageEntity packageEntity;

	@ManyToOne
	@JoinColumn(name = "shipment_type_id", nullable = false)
	private ShipmentType shipmentType;

	@ManyToOne
	@JoinColumn(name = "origin_location_id", nullable = false)
	private Location originLocation;

	@ManyToOne
	@JoinColumn(name = "destination_location_id", nullable = false)
	private Location destinationLocation;

	@Column(name = "freight_value", nullable = false)
	private int freightValue;

	@ManyToOne
	@JoinColumn(name = "delivery_person_id")
	private Employee deliveryPerson;

	@Enumerated(EnumType.STRING)
	@Column(name = "current_status", nullable = false)
	private ShipmentStatus currentStatus = ShipmentStatus.REGISTERED;

	@Column(name = "is_deleted", nullable = false)
	private boolean isDeleted = false;

	@Column(name = "shipment_date", nullable = false)
	private OffsetDateTime shipmentDate;

	@Column(name = "completion_date")
	private OffsetDateTime completionDate;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private OffsetDateTime updatedAt;
}
