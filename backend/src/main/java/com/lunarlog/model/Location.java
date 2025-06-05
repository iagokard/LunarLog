package com.lunarlog.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.OffsetDateTime;

@Entity
@Table(name = "locations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 8)
	private String cep;

	@Column(nullable = false, length = 100)
	private String street;

	@Column(nullable = false, length = 10)
	private String number;

	@Column(length = 100)
	private String complement;

	@Column(nullable = false, length = 100)
	private String neighborhood;

	@Column(nullable = false, length = 130)
	private String city;

	@Column(nullable = false, length = 2)
	private String state;

	@Column(name = "is_deleted", nullable = false)
	@JsonIgnore
	private boolean isDeleted = false;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private OffsetDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private OffsetDateTime updatedAt;
}
