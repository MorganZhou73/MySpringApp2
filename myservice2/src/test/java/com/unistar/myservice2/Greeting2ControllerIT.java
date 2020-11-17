package com.unistar.myservice2;

import com.unistar.myservice2.controllers.GreetingController;
import com.unistar.myservice2.model.Greeting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.net.URL;
import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * full-stack integration test
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class Greeting2ControllerIT {
	@LocalServerPort
	private int port;

	private URL base;

	@Autowired
	private TestRestTemplate restTemplate;

	@BeforeEach
	public void setUp() throws Exception {
		this.base = new URL("http://localhost:" + port);
		System.out.println("Greeting2ControllerIT base = " + base.toString());
	}

	@Test
	void getHello() throws Exception {
		// without MockMvc, need to add .antMatchers("/hello").permitAll() in configure()
		// otherwise, the response would be login page instead of HelloService
		ResponseEntity<String> response = restTemplate.getForEntity(base.toString() + "/hello",
				String.class);
		assertThat(response.getBody()).isEqualTo("Hello, World");
	}

	@Test
	@WithMockUser
	public void verify_NoCrossOrignUrl() throws Exception {
		ResponseEntity<String> response = this.restTemplate.exchange(
				RequestEntity.get(uri("/hello")).header(HttpHeaders.ORIGIN, "http://localhost:8080").build(),
				String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(null, response.getHeaders().getAccessControlAllowOrigin());

		assertThat(response.getBody()).isEqualTo("Hello, World");
	}

	@Test
	@WithMockUser
	public void corsWithAnnotation() throws Exception {
		ResponseEntity<Greeting> entity = this.restTemplate.exchange(
				RequestEntity.get(uri("/greeting")).header(HttpHeaders.ORIGIN, "http://localhost:8080").build(),
				Greeting.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		assertEquals("http://localhost:8080", entity.getHeaders().getAccessControlAllowOrigin());

		Greeting greeting = entity.getBody();
		String expected = String.format(GreetingController.TEMPLATE1, "World");
		assertEquals(expected, greeting.getContent());
	}

	@Test
	@WithMockUser
	public void corsWithJavaconfig() throws Exception {
		ResponseEntity<Greeting> entity = this.restTemplate.exchange(
				RequestEntity.get(uri("/greeting-javaconfig")).header(HttpHeaders.ORIGIN, "http://localhost:8080").build(),
				Greeting.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		assertEquals("http://localhost:8080", entity.getHeaders().getAccessControlAllowOrigin());

		Greeting greeting = entity.getBody();
		String expected = String.format(GreetingController.TEMPLATE1, "World");
		assertEquals(expected, greeting.getContent());
	}

	private URI uri(String path) {
		return restTemplate.getRestTemplate().getUriTemplateHandler().expand(path);
	}

}
