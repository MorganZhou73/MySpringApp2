package com.unistar.myservice2;

import com.unistar.myservice2.model.Employees;
import com.unistar.myservice2.repos.EmployeesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.text.SimpleDateFormat;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureJsonTesters
@SpringBootTest
@AutoConfigureMockMvc
class EmployeesTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private EmployeesRepository employeesRepository;

	// This object will be initialized by @AutoConfigureJsonTesters
	@Autowired
	private JacksonTester<Employees> jsonEmployees;

	private String testUseLastName = "Baggins";

	@BeforeEach
	public void setup() throws Exception {
		// when test on existing database, only delete our test record
		//employeesRepository.deleteAll();

		List<Employees> employeesList = employeesRepository.findByLastName(testUseLastName);
		for(Employees temp: employeesList) {
			String location = "/employee/" + temp.getId();
			mockMvc.perform(delete(location));
		}
	}

	private Employees newTestEmployee() throws Exception{
		SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
		return new Employees(100, "Frodo",testUseLastName,
				"Sales Rep", dataFormat.parse("1970-01-23"),
				dataFormat.parse("2020-02-14"),
				"112 Keele St.", "Toronto", 2);
	}

	// if there is no index.html, the auto-generated homepage will have the RepositoryIndex $._links.employee
	// @Test
	void shouldReturnRepositoryIndex() throws Exception {

		mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk()).andExpect(
				jsonPath("$._links.employee").exists());
	}

	@Test
	@WithMockUser
	void shouldCreateEntity() throws Exception {
		Employees employee = newTestEmployee();
		String postContent1 = jsonEmployees.write(employee).getJson();

//		String postContent1 = "{\"employeeID\": 100, \"firstName\": \"Frodo\", \"lastName\":\"Baggins\"" +
//				", \"title\": \"Sales Rep\", \"birthDate\": \"1970-01-23\"" +
//				", \"city\": \"Toronto\"" +
//				", \"reportsTo\": 2}";

//		MockHttpServletResponse response = mockMvc.perform(post("/employee").
//				contentType(MediaType.APPLICATION_JSON).content(postContent1))
//				.andReturn().getResponse();
//		assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

		mockMvc.perform(post("/employee")
				.contentType(MediaType.APPLICATION_JSON).content(postContent1))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", containsString("employee/")));
	}

	@Test
	@WithMockUser
	void shouldRetrieveEntity() throws Exception {
		Employees employee = newTestEmployee();
		String postContent1 = jsonEmployees.write(employee).getJson();

		MvcResult mvcResult = mockMvc.perform(post("/employee")
				.contentType(MediaType.APPLICATION_JSON).content(postContent1))
				.andExpect(status().isCreated())
				.andReturn();

		String location = mvcResult.getResponse().getHeader("Location");
		mockMvc.perform(get(location)).andExpect(status().isOk())
				.andExpect(jsonPath("$.employeeID").value(employee.getEmployeeID()))
				.andExpect(jsonPath("$.firstName").value(employee.getFirstName()))
				.andExpect(jsonPath("$.lastName").value(employee.getLastName()));
	}

	@Test
	@WithMockUser
	void shouldQueryEntity() throws Exception {
		Employees employee = newTestEmployee();
		String postContent1 = jsonEmployees.write(employee).getJson();

		MvcResult mvcResult = mockMvc.perform(post("/employee")
				.contentType(MediaType.APPLICATION_JSON).content(postContent1))
				.andExpect(status().isCreated())
				.andReturn();

		mockMvc.perform(get("/employee/search/findByLastName?name={name}", testUseLastName))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._embedded.employee[0].employeeID").value(employee.getEmployeeID()));
	}


	@Test
	@WithMockUser
	void shouldUpdateEntity() throws Exception {
		Employees employee = newTestEmployee();
		String postContent1 = jsonEmployees.write(employee).getJson();

		MvcResult mvcResult = mockMvc.perform(post("/employee")
				.contentType(MediaType.APPLICATION_JSON).content(postContent1))
				.andExpect(status().isCreated())
				.andReturn();

		String location = mvcResult.getResponse().getHeader("Location");

		mockMvc.perform(put(location).content("{\"firstName\": \"Bilbo\", \"lastName\":\"Baggins\"}"))
				.andExpect(status().isNoContent());

		mockMvc.perform(get(location))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.firstName").value("Bilbo"))
				.andExpect(jsonPath("$.lastName").value("Baggins"));
	}

	@Test
	@WithMockUser
	void shouldPartiallyUpdateEntity() throws Exception {
		Employees employee = newTestEmployee();
		String postContent1 = jsonEmployees.write(employee).getJson();

		MvcResult mvcResult = mockMvc.perform(post("/employee")
				.contentType(MediaType.APPLICATION_JSON).content(postContent1))
				.andExpect(status().isCreated())
				.andReturn();

		String location = mvcResult.getResponse().getHeader("Location");

		mockMvc.perform(patch(location).content("{\"firstName\": \"Bilbo Jr.\"}"))
				.andExpect(status().isNoContent());

		mockMvc.perform(get(location))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.firstName").value("Bilbo Jr."))
				.andExpect(jsonPath("$.lastName").value(employee.getLastName()));
	}

	@Test
	@WithMockUser
	void shouldDeleteEntity() throws Exception {
		Employees employee = newTestEmployee();
		String postContent1 = jsonEmployees.write(employee).getJson();

		MvcResult mvcResult = mockMvc.perform(post("/employee")
				.contentType(MediaType.APPLICATION_JSON).content(postContent1))
				.andExpect(status().isCreated())
				.andReturn();

		String location = mvcResult.getResponse().getHeader("Location");
		mockMvc.perform(delete(location))
				.andExpect(status().isNoContent());

		mockMvc.perform(get(location))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser
	void findByPropertiesTest() throws Exception {
		String uri = "/employee/search/findByProperties?city=London&page=1&size=2";
		mockMvc.perform(get(uri))
				.andDo(print())
				.andExpect(status().isOk());
	}
}
