package com.unistar.myservice2.controllers;

import com.unistar.myservice2.model.EmployeesModelAssembler;
import com.unistar.myservice2.exception.EmployeeExistedException;
import com.unistar.myservice2.exception.EmployeeNotFoundException;
import com.unistar.myservice2.model.Employees;
import com.unistar.myservice2.repos.EmployeesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin("*")
public class EmployeesController {
	private final EmployeesRepository repository;
	private final EmployeesModelAssembler assembler;

	@Autowired
	private PagedResourcesAssembler<Employees> pagedResourcesAssembler;

	@Autowired
	public EmployeesController(EmployeesRepository repository, EmployeesModelAssembler assembler) {
		this.repository = repository;
		this.assembler = assembler;
	}

	// Aggregate root

	@GetMapping("/employees")
	public CollectionModel<EntityModel<Employees>> all() {
		//if return repository.findAll() directly, works but not RESTfull service

		List<EntityModel<Employees>> employees = repository.findAll().stream()
				.map(assembler::toModel)
				.collect(Collectors.toList());

		return CollectionModel.of(employees,
				linkTo(methodOn(EmployeesController.class).all()).withSelfRel());
	}

	@PostMapping("/employees")
	public ResponseEntity<?> newEmployee(@RequestBody Employees newEmployee) {
		Optional<Employees> res = repository.findByEmployeeID(newEmployee.getEmployeeID());
		if(res.isPresent()){
			throw new EmployeeExistedException(newEmployee.getEmployeeID()) ;
		}else{
			EntityModel<Employees> entityModel = assembler.toModel(repository.save(newEmployee));

			return ResponseEntity //
					.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
					.body(entityModel);
		}
	}

	// Single item

	@GetMapping("/employees/{employeeID}")
	public EntityModel<Employees> one(@PathVariable int employeeID){
		Employees employee = repository.findByEmployeeID(employeeID) //
				.orElseThrow(() -> new EmployeeNotFoundException(employeeID));

		// if return employee directly, it works, but not a RESTfull service
		return assembler.toModel(employee);
	}

	@PutMapping("/employees/{employeeID}")
	public ResponseEntity<?> replaceEmployee(@RequestBody Employees newEmployee, @PathVariable int employeeID) {
		newEmployee.setEmployeeID(employeeID);

		Employees updatedEmployee = repository.findByEmployeeID(employeeID) //
				.map(employee -> {
					employee.copyAvailableValues(newEmployee);
					return repository.save(employee);
				})
				.orElseGet(() -> repository.save(newEmployee));

		EntityModel<Employees> entityModel = assembler.toModel(updatedEmployee);

		return ResponseEntity //
				.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
				.body(entityModel);
	}

	@DeleteMapping("/employees/{employeeID}")
	public ResponseEntity<?> deleteEmployee(@PathVariable int employeeID) {
		repository.findByEmployeeID(employeeID)
				.map(employee -> {
					repository.delete(employee);
					return employee;
				});

		return ResponseEntity.noContent().build();
	}

	@GetMapping("/employees/findByProperties")
	public ResponseEntity<?> getByProperties(
			@RequestParam(required = false) String firstName,
			@RequestParam(required = false) String lastName,
			@RequestParam(required = false) String title,
			@RequestParam(required = false) String city,
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Employees> employeesPage = repository.findByProperties(firstName, lastName, title, city, pageable);
		PagedModel<EntityModel<Employees>> employeesPageModel = pagedResourcesAssembler
				.toModel(employeesPage, this.assembler);

//		HttpHeaders responseHeaders = new HttpHeaders();
//		responseHeaders.add("Link", createLinkHeader(employeesPageModel));

		return new ResponseEntity<>(employeesPageModel, HttpStatus.OK);
	}

	@GetMapping("/employees/findByPropertiesA")
	@ResponseStatus(HttpStatus.OK)
	public CollectionModel<EntityModel<Employees>> getByPropertiesA(
			@RequestParam(required = false) String firstName,
			@RequestParam(required = false) String lastName,
			@RequestParam(required = false) String title,
			@RequestParam(required = false) String city,
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size) {
		// this method will lost page info
		// List<Employees> employeesList = repository.findByProperties(firstName, lastName, title, city, pageable);
		Pageable pageable = PageRequest.of(page, size);
		List<Employees> employeesList = repository.findByProperties(firstName, lastName, title, city, pageable).getContent();
		return this.assembler.toCollectionModel(employeesList);
	}

	private String createLinkHeader(PagedModel<EntityModel<Employees>> pr) {
		final StringBuilder linkHeader = new StringBuilder();
		if(!pr.getLinks("first").isEmpty())
			linkHeader.append(buildLinkHeader(pr.getLinks("first").get(0).getHref(), "first"));
		if(!pr.getLinks("next").isEmpty()){
			linkHeader.append(", ");
			linkHeader.append(buildLinkHeader(pr.getLinks("next").get(0).getHref(), "next"));
		}
		return linkHeader.toString();
	}

	public static String buildLinkHeader(final String uri, final String rel) {
		return "<" + uri + ">; rel=\"" + rel + "\"";
	}
}
