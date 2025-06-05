package com.lunarlog.repository;

import com.lunarlog.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

	Optional<Participant> findByCpf(String cpf);

	List<Participant> findByEmail(String email);

	List<Participant> findByLocationId(Long locationId);

	List<Participant> findByIsDeletedFalse();

	@Query("SELECT p FROM Participant p WHERE LOWER(p.fullName) LIKE LOWER(CONCAT('%', :name, '%'))")
	List<Participant> searchByName(String name);

	@Modifying
	@Query("UPDATE Participant p SET p.isDeleted = true WHERE p.id = :id")
	void softDelete(Long id);
}
