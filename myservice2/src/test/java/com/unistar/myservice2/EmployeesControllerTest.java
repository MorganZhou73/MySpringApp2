package com.unistar.myservice2;

import com.unistar.myservice2.controllers.EmployeesController;
import com.unistar.myservice2.model.Employees;
import com.unistar.myservice2.model.EmployeesModelAssembler;
import com.unistar.myservice2.repos.EmployeesRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureJsonTesters
@SpringBootTest
@AutoConfigureMockMvc
class EmployeesControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private EmployeesRepository employeesRepository;

	@Autowired
	private EmployeesModelAssembler assembler;

	@Autowired
	private EmployeesController controller;

	// This object will be initialized by @AutoConfigureJsonTesters
	@Autowired
	private JacksonTester<Employees> jsonEmployees;

	private String testUseLastName = "Baggins";

	@BeforeEach
	public void setup() throws Exception {
		// if not use @AutoConfigureJsonTesters, neet init JacksonTester
		// JacksonTester.initFields(this, new ObjectMapper());

		// when test on existing database, only delete our test record
		//employeesRepository.deleteAll();

		List<Employees> employeesList = employeesRepository.findByLastName(testUseLastName);
		for(Employees temp: employeesList) {
			String location = "/employee/" + temp.getId();
			mockMvc.perform(delete(location));
		}
	}

	@Test
	void contextLoads() throws Exception {
		Assertions.assertThat(controller).isNotNull();
	}

	@Test
	@WithMockUser
	void shouldCreateEntity() throws Exception {
		SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
		Employees employee = new Employees(100, "Frodo",testUseLastName,
				"Sales Rep", dataFormat.parse("1970-01-23"),
				dataFormat.parse("2020-02-14"),
				"112 Keele St.", "Toronto", 2);
		String postContent1 = jsonEmployees.write(employee).getJson();

		mockMvc.perform(post("/employees").
				contentType(MediaType.APPLICATION_JSON).content(postContent1))
				.andExpect(status().isCreated())
				// after created, response.getContentAsString() will have "id" and "_links"
				.andExpect(jsonPath("$.id", notNullValue()))
				.andExpect(jsonPath("$.employeeID").value(employee.getEmployeeID()))
				.andExpect(jsonPath("$.firstName").value(employee.getFirstName()))
				.andExpect(jsonPath("$.lastName").value(employee.getLastName()));

		// create with same employeeID the 2nd time will get CONFLICT exception
		MockHttpServletResponse response = mockMvc.perform(post("/employees").
				contentType(MediaType.APPLICATION_JSON).content(postContent1))
				.andReturn().getResponse();
		assertThat(response.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
	}

	@Test
	@WithMockUser
	void findByPropertiesTest() throws Exception {
		MockHttpServletResponse response = mockMvc.perform(
				get("/employees/findByProperties?city=London&page=1&size=2")
						.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	}
}
