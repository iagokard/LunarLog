package com.lunarlog.repository;

import com.lunarlog.model.Employee;
import com.lunarlog.model.enums.EmployeeRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	Optional<Employee> findByCpf(String cpf);

	Optional<Employee> findByEmail(String email);

	List<Employee> findByRole(EmployeeRole role);

	List<Employee> findByIsDeletedFalse();

	@Query("SELECT e FROM Employee e WHERE LOWER(e.fullName) LIKE LOWER(CONCAT('%', :name, '%'))")
	List<Employee> searchByName(String name);

	@Modifying
	@Query("UPDATE Employee e SET e.isDeleted = true WHERE e.id = :id")
	void softDelete(Long id);
}
