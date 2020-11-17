package com.unistar.myservice2;

import com.unistar.myservice2.model.Person;
import com.unistar.myservice2.repos.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/snippets")
class PersonTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private PersonRepository personRepository;

	@BeforeEach
	public void deleteAllBeforeTests() throws Exception {
		personRepository.deleteAll();
	}

	// if there is no index.html, the auto-generated homepage will have the RepositoryIndex $._links.people
	// @Test
	// @WithMockUser
	void shouldReturnRepositoryIndex() throws Exception {

		mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk()).andExpect(
				jsonPath("$._links.people").exists())
				.andDo(document("index",
				links(
						linkWithRel("people").description("The <<resources_people, people resource>>"),
						linkWithRel("employee").description("The <<resources_employee,employee resource>>"),
						linkWithRel("profile").description("The ALPS profile for the service")),
				responseFields(
						subsectionWithPath("_links").description("<<resources_index_access_links,Links>> to other resources"))));
	}

	@Test
	@WithMockUser
	void shouldCreateEntity() throws Exception {

		mockMvc.perform(post("/people").content(
				"{\"firstName\": \"Frodo\", \"lastName\":\"Baggins\"}")).andExpect(
				status().isCreated()).andExpect(
				header().string("Location", containsString("people/")));
	}

	@Test
	@WithMockUser
	void shouldRetrieveEntity() throws Exception {

		MvcResult mvcResult = mockMvc.perform(post("/people").content(
				"{\"firstName\": \"Frodo\", \"lastName\":\"Baggins\"}")).andExpect(
				status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");
		mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
				jsonPath("$.firstName").value("Frodo")).andExpect(
				jsonPath("$.lastName").value("Baggins"));
	}

	@Test
	@WithMockUser
	void shouldQueryEntity() throws Exception {

		mockMvc.perform(post("/people").content(
				"{ \"firstName\": \"Frodo\", \"lastName\":\"Baggins\"}")).andExpect(
				status().isCreated());

		mockMvc.perform(
				get("/people/search/findByLastName?name={name}", "Baggins")).andExpect(
				status().isOk()).andExpect(
				jsonPath("$._embedded.people[0].firstName").value(
						"Frodo"));
	}

	@Test
	@WithMockUser
	void shouldUpdateEntity() throws Exception {

		MvcResult mvcResult = mockMvc.perform(post("/people").content(
				"{\"firstName\": \"Frodo\", \"lastName\":\"Baggins\"}")).andExpect(
				status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");

		mockMvc.perform(put(location).content(
				"{\"firstName\": \"Bilbo\", \"lastName\":\"Baggins\"}")).andExpect(
				status().isNoContent());

		mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
				jsonPath("$.firstName").value("Bilbo")).andExpect(
				jsonPath("$.lastName").value("Baggins"));
	}

	@Test
	@WithMockUser
	void shouldPartiallyUpdateEntity() throws Exception {

		MvcResult mvcResult = mockMvc.perform(post("/people").content(
				"{\"firstName\": \"Frodo\", \"lastName\":\"Baggins\"}")).andExpect(
				status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");

		mockMvc.perform(
				patch(location).content("{\"firstName\": \"Bilbo Jr.\"}")).andExpect(
				status().isNoContent());

		mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
				jsonPath("$.firstName").value("Bilbo Jr.")).andExpect(
				jsonPath("$.lastName").value("Baggins"));
	}

	@Test
	@WithMockUser
	void shouldDeleteEntity() throws Exception {

		MvcResult mvcResult = mockMvc.perform(post("/people").content(
				"{ \"firstName\": \"Bilbo\", \"lastName\":\"Baggins\"}")).andExpect(
				status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");
		mockMvc.perform(delete(location)).andExpect(status().isNoContent());

		mockMvc.perform(get(location)).andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser
	void peopleListExample() throws Exception {
		//this.personRepository.deleteAll();

		createPerson("fn1", "ln1");
		createPerson("fn2", "ln2");
		createPerson("fn3", "ln3");

		this.mockMvc.perform(get("/people"))
				.andExpect(status().isOk())
				.andDo(document("people-list-example",
						links(
								linkWithRel("self").description("Canonical link for this resource"),
								linkWithRel("profile").description("The ALPS profile for this resource"),
								linkWithRel("search").description("The search link for this resource")),
						responseFields(
								subsectionWithPath("_embedded.people").description("An array of <<resources_people, people resources>>"),
								subsectionWithPath("_links").description("<<resources_people_list_links, Links>> to other resources"),
								subsectionWithPath("page").description("page split information")
								)));
	}

	private void createPerson(String firstName, String lastName) {
		Person person = new Person();
		person.setFirstName(firstName);
		person.setLastName(lastName);

		this.personRepository.save(person);
	}
}
