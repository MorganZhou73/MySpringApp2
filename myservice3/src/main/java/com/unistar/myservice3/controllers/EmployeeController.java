package com.unistar.myservice3.controllers;


import com.unistar.myservice3.exception.ResourceNotFoundException;
import com.unistar.myservice3.model.Employee;
import com.unistar.myservice3.model.EmployeeDTO;
import com.unistar.myservice3.services.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private ModelMapper modelMapper;

	// Aggregate root

	@GetMapping("/employees")
	public List<EmployeeDTO> all() {
        List<Employee> employees = employeeService.findAll();
		return employees.stream().map(this::convertToDto)
                .collect(Collectors.toList());
	}

	@PostMapping("/employees")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public EmployeeDTO createEmployee(@RequestBody EmployeeDTO employeeDto) {
        Employee employee = convertToEntity(employeeDto);
        Employee created = employeeService.save(employee);
        return convertToDto(created);
	}

	// Single item

	@GetMapping("/employees/{id}")
	public ResponseEntity<EmployeeDTO> one(@PathVariable(value = "id") Integer employeeID){
		Employee employee = employeeService.findById(employeeID)
				.orElseThrow(() -> new ResourceNotFoundException("employeeID = " + employeeID));

		EmployeeDTO dto = convertToDto(employee);
		return ResponseEntity.ok().body(dto);
	}

	@PutMapping("/employees/{id}")
	public ResponseEntity<EmployeeDTO> replaceEmployee(@RequestBody EmployeeDTO employeeDto,
                                                    @PathVariable(value = "id") Integer employeeID) {
        employeeDto.setEmployeeID(employeeID);
        Employee employee = convertToEntity(employeeDto);

		Employee updated = employeeService.findById(employeeID) //
				.map(emp -> {
					emp.copyAvailableValues(employee);
					return employeeService.save(emp);
				})
				.orElseGet(() -> employeeService.save(employee));

		return ResponseEntity.ok(convertToDto(updated));
	}

	@DeleteMapping("/employees/{id}")
	public ResponseEntity<String> deleteEmployee(@PathVariable(value = "id") Integer employeeID) {
		if( !employeeService.deleteById(employeeID))
			throw new ResourceNotFoundException("employeeID = " + employeeID);

		return ResponseEntity.noContent().build();
	}

	private EmployeeDTO convertToDto(Employee employee) {
		// here is the example of 2 converting-approches between Entity and DTO
		// return new EmployeeDTO(employee);
		return modelMapper.map(employee, EmployeeDTO.class);
	}

	private Employee convertToEntity(EmployeeDTO employeeDto) {
		// return employeeDto.toEntity();
		return modelMapper.map(employeeDto, Employee.class);
	}
}
