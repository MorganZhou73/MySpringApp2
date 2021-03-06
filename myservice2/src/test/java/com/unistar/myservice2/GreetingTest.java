/*
* Test only the web layer by using JUnit and Spring’s MockMvc.
* use the same tests to generate documentation for the API by using Spring REST Docs
* */
package com.unistar.myservice2;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.unistar.myservice2.controllers.GreetingController;
import com.unistar.myservice2.model.Greeting;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureJsonTesters
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/snippets")
class GreetingTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private GreetingController controller;

	// This object will be initialized by @AutoConfigureJsonTesters
	@Autowired
	private JacksonTester<Greeting> jsonMessage;

	@Test
	void contextLoads() throws Exception {
		assertThat(controller).isNotNull();
	}

	@Test
	@WithMockUser
	void shouldReturnDefaultMessage() throws Exception {
		this.mockMvc.perform(get("/greeting")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("Good morning, World!")))
				.andDo(document("greeting"));
	}

	@Test
	@WithMockUser
	void sendmessageString() throws Exception {
		Greeting message = new Greeting(1, "Good morning");
		String postContent1 = message.toString();

		mockMvc.perform(post("/sendmessage").
				contentType(MediaType.TEXT_PLAIN).content(postContent1))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString(GreetingController.MSG_RECEIVED_STRING + message.toString())));
	}

	@Test
	@WithMockUser
	void sendmessageJson() throws Exception {
		Greeting message = new Greeting(1, "Good morning");
		String postContent1 = jsonMessage.write(message).getJson();

		mockMvc.perform(post("/sendmessage").
				contentType(MediaType.APPLICATION_JSON).content(postContent1))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString(GreetingController.MSG_RECEIVED_JSON + message.toString())));
	}

	@Test
	@WithMockUser
	void sendmessageXml() throws Exception {
		Greeting message = new Greeting(1, "Good morning");
		XmlMapper xmlMapper = new XmlMapper();
		String postContent1 = xmlMapper.writeValueAsString(message);
		// "<message><id>1</id><content>Good morning</content></message>"

		mockMvc.perform(post("/sendmessage").
				contentType(MediaType.APPLICATION_XML).content(postContent1))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString(GreetingController.MSG_RECEIVED_XML + message.toString())));
	}

	@Test
	@WithMockUser
	void sendmessageMap() throws Exception {
		Greeting message = new Greeting(1, "Good morning");
		String postContent1 = String.format("id=%d&content=%s", message.getId(), message.getContent());

		mockMvc.perform(post("/sendmessagemap").
				contentType(MediaType.APPLICATION_FORM_URLENCODED).content(postContent1))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString(GreetingController.MSG_RECEIVED_MAP + message.toString())));
	}

}
