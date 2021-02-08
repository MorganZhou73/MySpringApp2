package com.unistar.myservice3;

import com.unistar.myservice3.model.Address;
import com.unistar.myservice3.model.User;
import com.unistar.myservice3.repos.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserOrdersControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	private String testEmail = "joe@gmail.com";
	private Integer testUserId = 0;

	@BeforeEach
	public void setup() throws Exception {
		userRepository.deleteAll();
		User user = new User("Joe", testEmail);

		Address address1 = new Address();
		address1.setCity("Toronto");
		address1.setUser(user);
		user.getAddresses().add(address1);

		Address address2 = new Address();
		address2.setCity("Markham");
		address2.setUser(user);
		user.getAddresses().add(address2);

		userRepository.save(user);
		testUserId = user.getId();
	}

	@Test
	void getUserByIdTest() throws Exception {
		String location = "/users/" + testUserId ;
		MockHttpServletResponse response = mockMvc.perform(
				get(location).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(testUserId))
				.andExpect(jsonPath("$.email").value(testEmail))
				.andReturn().getResponse();

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		System.out.println(response.getContentAsString());

		location = "/users/" + "12345" ;
		mockMvc.perform(
				get(location).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void getUserByEmailTest() throws Exception {
		String location = "/users/get?email=" + testEmail ;
		MockHttpServletResponse response = mockMvc.perform(
				get(location).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(testUserId))
				.andExpect(jsonPath("$.email").value(testEmail))
				.andReturn().getResponse();

		location = "/users/get?email=" + "unknown@aa" ;
		mockMvc.perform(
				get(location).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void addNewUserTest() throws Exception {
		String name = "newUserName";
		String email = "newMail@aa.com";
		String location = "/users/add?name=" + name + "&email=" + email;

		MvcResult mvcResult = mockMvc.perform(post(location).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andReturn();

		String sUrl = mvcResult.getResponse().getHeader("Location");
		System.out.println("created user = " + sUrl);
	}

	@Test
	void getUsersTest() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/").accept(MediaType.TEXT_HTML))
				.andExpect(status().isOk())
				//.andDo(print())
				.andReturn();

		mvcResult = mockMvc.perform(get("/users").accept(MediaType.TEXT_HTML))
				.andExpect(status().isOk())
				.andDo(print())
				.andReturn();

		//System.out.println(mvcResult.getResponse().getContentAsString());
	}
}
