package com.unistar.myservice3.controllers;

import com.unistar.myservice3.exception.ResourceConflictException;
import com.unistar.myservice3.exception.ResourceNotFoundException;
import com.unistar.myservice3.model.User;
import com.unistar.myservice3.model.UserDTO;
import com.unistar.myservice3.services.UserOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;

@Controller
public class UserOrdersController {
	@Autowired
	private UserOrdersService userOrdersService;

	@GetMapping("/users")
	public String getUsers(Model model)
	{
		// return String means a view name .html

		model.addAttribute("users", userOrdersService.getUsers());
		model.addAttribute("orders", userOrdersService.getOrders());

		return "users";
	}

	@PostMapping("/users/add")
	public ResponseEntity<UserDTO> addNewUser (@RequestParam String name
			, @RequestParam String email) {
		// @ResponseBody means the returned String is the response, not a view name
		// @RequestParam means it is a parameter from the GET or POST request

		if(name.isEmpty() || email.isEmpty())
			return ResponseEntity.noContent().build();

		User newUser = userOrdersService.addUser(new User(name, email));
		if(newUser == null)
			throw new ResourceConflictException("email = " + email);

		return ResponseEntity.created(getUserUri(newUser.getId())).body(new UserDTO(newUser));
	}

	@GetMapping("/users/{id}")
	public ResponseEntity<UserDTO> getUserById(@PathVariable(value = "id") Integer userId){
		User user = userOrdersService.getUserById(userId) //
				.orElseThrow(() -> new ResourceNotFoundException("id = " + userId));

		// to prevent Infinite recursion JSON serialization
		return ResponseEntity.ok().body(new UserDTO(user));
	}

	@GetMapping("/users/get")
	public ResponseEntity<UserDTO> getUserByEmail(@RequestParam String email){
		List<User> users = userOrdersService.getUserByEmail(email);
		if(users.isEmpty())
			throw new ResourceNotFoundException("email = " + email);

		return ResponseEntity.ok().body(new UserDTO(users.get(0)));
	}

	private URI getUserUri(Integer userId){
		return linkTo(methodOn(UserOrdersController.class).getUserById(userId)).toUri();
	}
}
