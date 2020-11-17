package com.unistar.myservice3.services;

import com.unistar.myservice3.model.Employee;
import com.unistar.myservice3.repos.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
	@Autowired
	private EmployeeRepository repository;

	public Optional<Employee> findById(Integer employeeId){
		return repository.findById(employeeId);
	}

	public Employee save(Employee obj){
		return repository.save(obj);
	}

	public boolean deleteById(Integer employeeId){
		Optional<?> obj = repository.findById(employeeId)
				.map(employee -> {
					repository.delete(employee);
					return employee;
				});

		return obj.isPresent();
	}

	public List<Employee> findAll() {
		return repository.findAll();
	}

	public List<Employee> findByLastName(String lastName){
		return repository.findByLastName(lastName);
	}

	public List<Employee> findByFullName(String firstName, String lastName){
		return repository.findByFullName(firstName, lastName);
	}
}
