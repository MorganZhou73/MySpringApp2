package com.unistar.myservice2.repos;

import com.unistar.myservice2.model.Employees;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

public class EmployeesRepositoryCustomImpl implements EmployeesRepositoryCustom {
	@Autowired
	MongoTemplate mongoTemplate;

	public Page<Employees> findByProperties(String firstName, String lastName,
											String title, String city, Pageable pageable){
		Query query = new Query();
		final List<Criteria> criteria = new ArrayList<>();
		if (firstName != null && !firstName.isEmpty())
			criteria.add(Criteria.where("firstName").is(firstName));
		if (lastName != null && !lastName.isEmpty())
			criteria.add(Criteria.where("lastName").is(lastName));
		if (title != null && !title.isEmpty())
			criteria.add(Criteria.where("title").is(title));
		if (city != null && !city.isEmpty())
			criteria.add(Criteria.where("city").is(city));

		if (!criteria.isEmpty())
			query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])));

		int count = mongoTemplate.find(query, Employees.class).size();
		List<Employees> employees = mongoTemplate.find(query.with(pageable), Employees.class);
		return new PageImpl(employees, pageable, count);
	}
}
