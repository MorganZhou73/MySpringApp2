package com.unistar.myservice3.controllers;

import com.unistar.myservice3.model.EmployeeDTO;
import com.unistar.myservice3.services.RabbitMQSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/myservice3-rabbitmq/")
public class RabbitMQController {
	@Autowired
	RabbitMQSender rabbitMQSender;

	@GetMapping(value = "/employee")
	public String producer(@RequestParam("id") Integer employeeId, @RequestParam("firstName") String firstName
			, @RequestParam("lastName") String lastName) {

		EmployeeDTO employee = new EmployeeDTO(employeeId, firstName, lastName);
		rabbitMQSender.send(employee);

		return "Message sent to the RabbitMQ /employee Successfully: " + employee.toString();
	}

}
