package com.unistar.myservice2.repos;

import com.unistar.myservice2.model.Employees;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "employee", path = "employee")
public interface EmployeesRepository extends MongoRepository<Employees, String> , EmployeesRepositoryCustom {
	List<Employees> findByLastName(@Param("name") String name);
	Optional<Employees> findByEmployeeID(@Param("employeeID") int employeeID);

	@Query("{ $and: [ { 'FirstName' : { $regex: ?0 }}, { 'LastName' : { $regex: ?1 }} ] }")
	List<Employees> findByQueryFnLn(@Param("fn") String firstName, @Param("ln") String lastName);

	// Find multiple fields
	@RestResource(path="findByLnOrFn", rel="findByLnOrFn")
	List<Employees> findByLastNameOrFirstNameContainingAllIgnoreCase(@Param("name") String firstName,
																  @Param("name") String lastName);
}
