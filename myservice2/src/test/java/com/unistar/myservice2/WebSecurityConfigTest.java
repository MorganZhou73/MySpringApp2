package com.unistar.myservice2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WebSecurityConfigTest {
	@Autowired
	private MockMvc mockMvc;

	@Test
	void loginWithValidUserThenAuthenticated() throws Exception {
		SecurityMockMvcRequestBuilders.FormLoginRequestBuilder login = formLogin()
				.user("user1")
				.password("secret123");

		mockMvc.perform(login)
				.andExpect(authenticated().withUsername("user1"));
	}

	@Test
	void loginWithInvalidUserThenUnauthenticated() throws Exception {
		SecurityMockMvcRequestBuilders.FormLoginRequestBuilder login = formLogin()
				.user("invalid")
				.password("invalidpassword");

		mockMvc.perform(login)
				.andExpect(unauthenticated());
	}

	@Test
	void accessUnsecuredResourceThenOk() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isOk());
	}

	@Test
	void accessSecuredResourceUnauthenticatedThenRedirectsToLogin() throws Exception {
		mockMvc.perform(get("/hello2"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrlPattern("**/login"));
	}

	@Test
	@WithMockUser
	void accessSecuredResourceAuthenticatedThenOk() throws Exception {
		mockMvc.perform(get("/hello2"))
				.andExpect(status().isOk());
	}
}
