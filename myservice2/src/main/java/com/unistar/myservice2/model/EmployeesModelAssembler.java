package com.unistar.myservice2.model;

import com.unistar.myservice2.controllers.EmployeesController;
import com.unistar.myservice2.model.Employees;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EmployeesModelAssembler implements RepresentationModelAssembler<Employees, EntityModel<Employees>> {

	@Override
	public EntityModel<Employees> toModel(Employees employee) {

		return EntityModel.of(employee,
				linkTo(methodOn(EmployeesController.class).one(employee.getEmployeeID())).withSelfRel(),
				linkTo(methodOn(EmployeesController.class).all()).withRel("employees"));
	}

	@Override
	public CollectionModel<EntityModel<Employees>> toCollectionModel(Iterable<? extends Employees> entities)
	{
		// convert Iterable<? extends Employees> to List<Employees>
		List<Employees> list  = new ArrayList<>();
		entities.forEach(list::add);

		List<EntityModel<Employees>> employees = list.stream()
				.map(this::toModel)
				.collect(Collectors.toList());
		 return CollectionModel.of(employees,
				linkTo(methodOn(EmployeesController.class).all()).withSelfRel());
	}
}
