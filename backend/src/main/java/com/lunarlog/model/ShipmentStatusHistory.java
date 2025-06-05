package com.lunarlog.model;

import com.lunarlog.model.enums.ShipmentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "shipment_status_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentStatusHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "shipment_id", nullable = false)
	private Long shipmentId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ShipmentStatus status;

	@ManyToOne
	@JoinColumn(name = "location_id", nullable = false)
	private Location location;

	@ManyToOne
	@JoinColumn(name = "next_location_id")
	private Location nextLocation;

	@CreationTimestamp
	@Column(name = "event_time", nullable = false, updatable = false)
	private OffsetDateTime eventTime;
}
