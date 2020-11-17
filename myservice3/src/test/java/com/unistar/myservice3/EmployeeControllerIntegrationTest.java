package com.unistar.myservice3;
import com.unistar.myservice3.model.Employee;
import com.unistar.myservice3.model.EmployeeDTO;
import com.unistar.myservice3.services.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureJsonTesters
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmployeeControllerIntegrationTest {
	@Autowired
	private TestRestTemplate restTemplate;

	// This object will be initialized by @AutoConfigureJsonTesters
	@Autowired
	private JacksonTester<EmployeeDTO> jsonEmployee;

	@Autowired
	private EmployeeService employeeService;

	@LocalServerPort
	private int port;

	private String testUseLastName = "testLastN";
	private Integer testId = 0;

	private String getRootUrl() {
		return "http://localhost:" + port;
	}

	@BeforeEach
	public void setUp() throws Exception {
		System.out.println("EmployeeControllerIntegrationTest base = " + getRootUrl());

		// clear and create new employee for test
		List<Employee> employeeList = employeeService.findByLastName(testUseLastName);
		for(Employee temp: employeeList) {
			employeeService.deleteById(temp.getEmployeeID());
		}

		Employee employee = newTestEmployee().toEntity();
		Employee saved = employeeService.save(employee);
		testId = saved.getEmployeeID();
	}

	private EmployeeDTO newTestEmployee(){
		EmployeeDTO dto = new EmployeeDTO(100, "TestFirstName", testUseLastName);
		dto.setTitle("Sales Rep");
		dto.setBirthDate("1970-01-23");
		//dto.setHireDate("2020-02-14");
		dto.setAddress("112 Keele St.");
		dto.setCity("Toronto");
		dto.setReportsTo(2);

		return dto;
	}

	@Test
	void testGetAllEmployees() {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/employees",
				HttpMethod.GET, entity, String.class);

		assertNotNull(response.getBody());
	}

	@Test
	void testGetEmployeeById() {
		EmployeeDTO employee = restTemplate.getForObject(getRootUrl() + "/employees/" + testId, EmployeeDTO.class);
		System.out.println(employee.getFirstName());
		assertNotNull(employee);
		assertEquals(testId, employee.getEmployeeID());
	}

	@Test
	void testCreateEmployee() throws Exception {
		EmployeeDTO employee = newTestEmployee();
		employee.setEmployeeID(10); // since @Id GenerationType.AUTO, no use to set manually
		employee.setFirstName("Joe");
		employee.setCity("Markham");

		ResponseEntity<EmployeeDTO> postResponse = restTemplate.postForEntity(getRootUrl() + "/employees", employee, EmployeeDTO.class);
		assertNotNull(postResponse);
		assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());
		EmployeeDTO created = postResponse.getBody();

		assertNotNull(created);
		assertNotNull(created.getEmployeeID());
		assertEquals(employee.getFirstName(), created.getFirstName());
		// this also verified the ModelMapper.stringToDate and dateToString
		assertEquals(employee.getBirthDate(), created.getBirthDate());

		// demo to test with JSON string post
		employee.setFirstName("Mary");
		employee.setTitle("Vice President");
		String postContent1 = jsonEmployee.write(employee).getJson();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<String>(postContent1, headers);

		postResponse = restTemplate.postForEntity(getRootUrl() + "/employees", request, EmployeeDTO.class);
		assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());
	}

	@Test
	void testUpdateEmployee() {
		EmployeeDTO employee = restTemplate.getForObject(getRootUrl() + "/employees/" + testId, EmployeeDTO.class);
		employee.setFirstName("admin1");
		employee.setBirthDate("1975-05-18");

//		restTemplate.put(getRootUrl() + "/employees/" + testId, employee);
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<EmployeeDTO> entity = new HttpEntity<EmployeeDTO>(employee, headers);
		ResponseEntity<EmployeeDTO> response = restTemplate.exchange(getRootUrl() + "/employees/" + testId,
				HttpMethod.PUT, entity, EmployeeDTO.class);

		EmployeeDTO updatedEmployee = restTemplate.getForObject(getRootUrl() + "/employees/" + testId, EmployeeDTO.class);
		assertNotNull(updatedEmployee);
		assertEquals(employee.getFirstName(), updatedEmployee.getFirstName());
		assertEquals(employee.getBirthDate(), updatedEmployee.getBirthDate());
	}

	@Test
	void testDeleteEmployee() {
		EmployeeDTO employee = restTemplate.getForObject(getRootUrl() + "/employees/" + testId, EmployeeDTO.class);
		assertNotNull(employee);
		assertEquals(testId, employee.getEmployeeID());

//		restTemplate.delete(getRootUrl() + "/employees/" + testId);
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/employees/" + testId,
				HttpMethod.DELETE, entity, String.class);
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

		ResponseEntity<?> result = restTemplate.getForEntity(getRootUrl() + "/employees/" + testId, String.class);
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

		// second time delete return NOT_FOUND: ResourceNotFoundException
		response = restTemplate.exchange(getRootUrl() + "/employees/" + testId,
				HttpMethod.DELETE, entity, String.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	private URI uri(String path) {
		return restTemplate.getRestTemplate().getUriTemplateHandler().expand(path);
	}
}
