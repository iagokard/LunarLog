package com.lunarlog.repository;

import com.lunarlog.model.ShipmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShipmentTypeRepository extends JpaRepository<ShipmentType, Long> {

	Optional<ShipmentType> findByName(String name);

	List<ShipmentType> findByIsDeletedFalse();

	@Query("SELECT st FROM ShipmentType st WHERE st.maxWeightExemption >= :weight")
	List<ShipmentType> findByApplicableForWeight(int weight);

	@Modifying
	@Query("UPDATE ShipmentType st SET st.isDeleted = true WHERE st.id = :id")
	void softDelete(Long id);
}
