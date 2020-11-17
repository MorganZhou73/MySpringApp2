package com.unistar.myservice3.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unistar.myservice3.model.Employee;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
	@Query("FROM Employee WHERE lastname=:lastname")
    List<Employee> findByLastName(@Param("lastname")String lastName);

	@Query("FROM Employee WHERE firstname=:firstname AND lastname=:lastname")
	List<Employee> findByFullName(@Param("firstname")String firstName, @Param("lastname")String lastName);
}
