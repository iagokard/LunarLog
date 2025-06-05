package com.lunarlog.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "shipment_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentType {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 50)
	private String name;

	@Column(name = "freight_per_weight", nullable = false)
	private int freightPerWeight;

	@Column(name = "freight_per_distance", nullable = false)
	private int freightPerDistance;

	@Column(name = "freight_per_volume", nullable = false)
	private int freightPerVolume;

	@Column(name = "max_weight_exemption")
	private Integer maxWeightExemption;

	@Column(name = "max_distance_exemption")
	private Integer maxDistanceExemption;

	@Column(name = "max_volume_exemption")
	private Integer maxVolumeExemption;

	@Column(name = "is_deleted", nullable = false)
	private boolean isDeleted = false;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private OffsetDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private OffsetDateTime updatedAt;
}
