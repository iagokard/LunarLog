package com.lunarlog.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Generated;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "packages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackageEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "height_cm", nullable = false, precision = 5, scale = 2)
	private BigDecimal heightCm;

	@Column(name = "width_cm", nullable = false, precision = 5, scale = 2)
	private BigDecimal widthCm;

	@Column(name = "depth_cm", nullable = false, precision = 5, scale = 2)
	private BigDecimal depthCm;

	@Column(name = "weight_grams", nullable = false)
	private int weightGrams;

	@Column(name = "volume_cm3", insertable = false, updatable = false)
	@Generated
	private Integer volumeCm3;

	@Column(columnDefinition = "TEXT")
	private String description;

	@Column(name = "is_deleted", nullable = false)
	private boolean isDeleted = false;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private OffsetDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private OffsetDateTime updatedAt;
}
