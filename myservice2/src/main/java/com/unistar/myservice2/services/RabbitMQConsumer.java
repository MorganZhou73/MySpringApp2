package com.unistar.myservice2.services;

import com.unistar.myservice2.model.EmployeeDTO;
import com.unistar.myservice2.model.Employees;
import com.unistar.myservice2.repos.EmployeesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Optional;

@Component
public class RabbitMQConsumer {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConsumer.class);

    @Autowired
    private EmployeesRepository employeesRepository;

    @RabbitListener(queues = "${myservice3.rabbitmq.queue}")
    synchronized public void recievedMessage(EmployeeDTO msg) throws ParseException{
        // default parameter type is org.springframework.amqp.core.Message
        // System.out.println("class name: " + msg.getClass().getName());
        System.out.println("Recieved Message From RabbitMQ: " + msg.toString());

        Employees newEmp = msg.toEntity();
        Optional<Employees> existed = employeesRepository.findByEmployeeID(msg.getEmployeeID())
                .map(emp -> {
                    logger.info("the same employee ID already exists: [{}, {}, {}]", emp.getId(),
                            emp.getFirstName(), emp.getLastName());
                    return emp;
                });

         if(!existed.isPresent()) {
             employeesRepository.save(newEmp);
             logger.info("the received employee doc is saved to MongoDB.");
         };
    }
}
