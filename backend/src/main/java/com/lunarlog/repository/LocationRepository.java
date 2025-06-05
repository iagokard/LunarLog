package com.lunarlog.repository;

import com.lunarlog.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

	Optional<Location> findByCepAndNumber(String cep, String number);

	List<Location> findByCep(String cep);

	List<Location> findByCity(String city);

	List<Location> findByIsDeletedFalse();

	@Query("SELECT l FROM Location l WHERE l.state = :state AND l.city = :city")
	List<Location> findByStateAndCity(String state, String city);

	@Modifying
	@Query("UPDATE Location l SET l.isDeleted = true WHERE l.id = :id")
	void softDelete(Long id);
}
