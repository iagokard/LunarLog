package com.lunarlog.repository;

import com.lunarlog.model.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackageRepository extends JpaRepository<PackageEntity, Long> {

	List<PackageEntity> findByIsDeletedFalse();

	@Query("SELECT p FROM PackageEntity p WHERE p.weightGrams BETWEEN :minWeight AND :maxWeight")
	List<PackageEntity> findByWeightRange(int minWeight, int maxWeight);

	@Query("SELECT p FROM PackageEntity p WHERE p.volumeCm3 BETWEEN :minVolume AND :maxVolume")
	List<PackageEntity> findByVolumeRange(int minVolume, int maxVolume);

	@Modifying
	@Query("UPDATE PackageEntity p SET p.isDeleted = true WHERE p.id = :id")
	void softDelete(Long id);
}
