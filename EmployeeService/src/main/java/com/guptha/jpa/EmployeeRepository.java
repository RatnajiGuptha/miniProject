package com.guptha.jpa;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.guptha.domain.Employee;


public interface EmployeeRepository extends JpaRepository<Employee,Integer> {

	List<Employee> findByBloodGroup(String bloodGroup);

	List<Employee> findByFirstName(String firstName);

	List<Employee> findByLastName(String lastName);

	List<Employee> findByFirstNameAndLastName(String firstName, String lastName);

	List<Employee> findByDateOfJoin(LocalDate dateOfJoin);

	


}
