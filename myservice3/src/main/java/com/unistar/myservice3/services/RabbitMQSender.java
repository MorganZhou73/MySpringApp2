package com.unistar.myservice3.services;

import com.unistar.myservice3.model.EmployeeDTO;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSender {
	@Autowired
	private AmqpTemplate rabbitTemplate;

	@Value("${myservice3.rabbitmq.exchange}")
	private String exchange;

	@Value("${myservice3.rabbitmq.routingkey}")
	private String routingkey;

	public void send(EmployeeDTO employee) {
		rabbitTemplate.convertAndSend(exchange, routingkey, employee);
		System.out.println("Send msg = " + employee);

	}
}
