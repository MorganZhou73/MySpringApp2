package com.unistar.myservice3.services;

import java.util.*;

import com.unistar.myservice3.model.UserDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unistar.myservice3.model.Order;
import com.unistar.myservice3.repos.OrderRepository;
import com.unistar.myservice3.model.User;
import com.unistar.myservice3.repos.UserRepository;

@Service
public class UserOrdersService
{
	private static final Logger logger = LoggerFactory.getLogger(UserOrdersService.class);

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private UserRepository userRepository;

	public List<User> getUsers()
	{
		return userRepository.findAll();
	}

	public List<User> getUserByEmail(String email)
	{
		List<User> users = new ArrayList<>();
		List<UserDTO> userDtos = userRepository.findByEmail(email);
		for (UserDTO temp: userDtos) {
			logger.info("UserDTO: {}", userDtos);
			users.add(new User(temp));
		}

		return users;
	}

	@Transactional
	public User addUser(User user)
	{
		if(user.getName().isEmpty() || user.getEmail().isEmpty())
			return null;

		List<User> users = getUserByEmail(user.getEmail());
		if(users.isEmpty())
			return userRepository.save(user);

		return null;
	}

	public List<Order> getOrders()
	{
		return orderRepository.findAll();
	}

	public Optional<Order> getOrderById(Integer orderId){
		return orderRepository.findById(orderId);
	}

	public Optional<User> getUserById(Integer userId){
		return userRepository.findById(userId);
	}
}
