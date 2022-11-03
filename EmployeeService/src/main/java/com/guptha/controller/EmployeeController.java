package com.guptha.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.guptha.Exception.EmployeeNotFoundException;
import com.guptha.domain.Employee;
import com.guptha.jpa.EmployeeRepository;

@RestController
public class EmployeeController {

	@Autowired
	EmployeeRepository repo;

	@GetMapping("/employees")
	public List<Employee> getAllEmployees() {
		return repo.findAll();
	}

	@GetMapping("/employeeById/{id}")
	public EntityModel<Employee> getEmployeeById(@PathVariable int id) {
		Optional<Employee> emp = repo.findById(id);

		if (emp.isEmpty())
			throw new EmployeeNotFoundException("Employee not found by id : " + id);

		EntityModel<Employee> entityModel = EntityModel.of(emp.get());
		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).getAllEmployeesWithLinks());
		entityModel.add(link.withRel("All-Emlpoyees"));

		return entityModel;
	}

	@GetMapping("/employees/links")
	public CollectionModel<EntityModel<Employee>> getAllEmployeesWithLinks() {

		List<EntityModel<Employee>> employees = repo.findAll().stream()
				.map(employee -> EntityModel.of(employee,
						linkTo(methodOn(this.getClass()).getEmployeeById(employee.getId())).withSelfRel(),
						linkTo(methodOn(this.getClass()).getAllEmployeesWithLinks()).withRel("All-Employees-links"),
						linkTo(methodOn(this.getClass()).getAllEmployees()).withRel("All-Employees-without-links")))
				.collect(Collectors.toList());

		return CollectionModel.of(employees,
				linkTo(methodOn(this.getClass()).getAllEmployeesWithLinks()).withSelfRel());
	}

	@GetMapping("/employeesBybloodGroup/{bloodGroup}")
	public CollectionModel<EntityModel<Employee>> getEmployeesByBloodGroup(@PathVariable String bloodGroup) {
		
		List<Employee> emp = repo.findByBloodGroup(bloodGroup);

		if (emp.isEmpty())
			throw new EmployeeNotFoundException("Employee not found by blood group : " + bloodGroup);

		List<EntityModel<Employee>> employees = repo.findByBloodGroup(bloodGroup).stream()
				.map(employee -> EntityModel.of(employee,
						linkTo(methodOn(this.getClass()).getAllEmployeesWithLinks()).withRel("employees")))
				.collect(Collectors.toList());

		return CollectionModel.of(employees,
				linkTo(methodOn(this.getClass()).getEmployeesByBloodGroup(bloodGroup)).withSelfRel());

	}

	@GetMapping("/employeesByFirstName/{firstName}")
	public CollectionModel<EntityModel<Employee>> getEmployeesByFirstName(@PathVariable String firstName) {
		List<Employee> emp = repo.findByFirstName(firstName);

		if (emp.isEmpty())
			throw new EmployeeNotFoundException("Employee not found by first name : " + firstName);

		List<EntityModel<Employee>> employees = repo.findByFirstName(firstName).stream()
				.map(employee -> EntityModel.of(employee,
						linkTo(methodOn(this.getClass()).getAllEmployeesWithLinks()).withRel("employees")))
				.collect(Collectors.toList());

		return CollectionModel.of(employees,
				linkTo(methodOn(this.getClass()).getEmployeesByFirstName(firstName)).withSelfRel());
	}

	@GetMapping("/employeesByLastName/{lastName}")
	public CollectionModel<EntityModel<Employee>> getEmployeesByLastName(@PathVariable String lastName) {
		List<Employee> emp = repo.findByLastName(lastName);

		if (emp.isEmpty())
			throw new EmployeeNotFoundException("Employee not found by last name : " + lastName);

		List<EntityModel<Employee>> employees = repo.findByLastName(lastName).stream()
				.map(employee -> EntityModel.of(employee,
						linkTo(methodOn(this.getClass()).getAllEmployeesWithLinks()).withRel("employees")))
				.collect(Collectors.toList());

		return CollectionModel.of(employees,
				linkTo(methodOn(this.getClass()).getEmployeesByLastName(lastName)).withSelfRel());
	}

	@GetMapping("/employeesByDateOfJoin/{dateOfJoin}")
	public CollectionModel<EntityModel<Employee>> getEmployeesByDateOfJoin(
			@PathVariable @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate dateOfJoin) {

		List<Employee> emp = repo.findByDateOfJoin(dateOfJoin);

		if (emp.isEmpty())
			throw new EmployeeNotFoundException("Employees not found by this joingdate: " + dateOfJoin);

		List<EntityModel<Employee>> empolyees = repo.findByDateOfJoin(dateOfJoin).stream()
				.map(employee -> EntityModel.of(employee,
						linkTo(methodOn(this.getClass()).getAllEmployeesWithLinks()).withRel("employees")))
				.collect(Collectors.toList());

		return CollectionModel.of(empolyees,
				linkTo(methodOn(this.getClass()).getEmployeesByDateOfJoin(dateOfJoin)).withSelfRel());
	}

	@GetMapping("/employee/firstName/{firstName}/lastName/{lastName}")
	public CollectionModel<EntityModel<Employee>> getEmployeeByName(@PathVariable String firstName,
			@PathVariable String lastName) {
		List<Employee> emp = repo.findByFirstNameAndLastName(firstName, lastName);

		if (emp.isEmpty())
			throw new EmployeeNotFoundException(
					"Employee not found by first name " + firstName + " and last name " + lastName);

		List<EntityModel<Employee>> empolyees = repo.findByFirstNameAndLastName(firstName, lastName).stream()
				.map(employee -> EntityModel.of(employee,
						linkTo(methodOn(this.getClass()).getAllEmployeesWithLinks()).withRel("employees")))
				.collect(Collectors.toList());

		return CollectionModel.of(empolyees,
				linkTo(methodOn(this.getClass()).getEmployeeByName(firstName, lastName)).withSelfRel());
	}

	@PostMapping("/addEmployee")
	public ResponseEntity<Employee> addEmployee(@Valid @RequestBody Employee e) {
		Employee savedEmp = repo.save(e);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedEmp.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}

	@DeleteMapping("/employeeById/{id}")
	public String deleteByEmployee(@PathVariable int id) {
		Optional<Employee> emp = repo.findById(id);
		if (emp.isEmpty())
			throw new EmployeeNotFoundException("Employee not found by id : " + id);
		repo.deleteById(id);

		return "Employee Deleted";
	}

}
