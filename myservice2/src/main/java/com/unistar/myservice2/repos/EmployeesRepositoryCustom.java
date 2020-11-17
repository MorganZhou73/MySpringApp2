package com.unistar.myservice2.repos;

import com.unistar.myservice2.model.Employees;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeesRepositoryCustom {
	public Page<Employees> findByProperties(String firstName, String lastName,
											String title, String city, Pageable pageable);
}
