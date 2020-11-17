package com.unistar.myservice2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
public class WebSecurityConfig {

	@Configuration
	@Order(1)
	public static class CrossWebSecurityConfig extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
					.cors().and()
					.requestMatchers().antMatchers("/greeting","/greeting-javaconfig")
					.and()
					.headers()
					.addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Origin", "http://localhost:8080"));
		}
	}

	@Configuration
	@Order(2)
	public static class ApiWebSecurityConfig extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
					//.cors().and()
					// disabling csrf here, you should enable it before using in production
					.csrf().disable()
					.authorizeRequests()
					.antMatchers("/", "/home").permitAll()
					.antMatchers("/hello").permitAll()
					.antMatchers("/greeting", "/greeting-javaconfig").permitAll()
					//.antMatchers("/people", "/people/**").permitAll()
					//.antMatchers("/employee", "/employees", "/employee/**", "/employees/**").permitAll()
					.anyRequest()
					//.hasRole("USER")
					.authenticated()
					.and()
					.formLogin()
					.loginPage("/login")
					.permitAll()
					.and()
					.logout().permitAll();
		}

		// passwordEncoder().encode("secret123") : "$2a$10$AIUufK8g6EFhBcumRRV2L.AQNz3Bjp7oDQVFiO5JJMBFZQ6x2/R/2";
		private static final String ENCODED_PASSWORD = passwordEncoder().encode("secret123");

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.inMemoryAuthentication()
					.passwordEncoder(passwordEncoder())
					.withUser("user1").password(ENCODED_PASSWORD).roles("USER");
		}

		@Bean
		public static PasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}

//	// this bean could be used only for demos
//	@Bean
//	@Override
//	public UserDetailsService userDetailsService() {
//		UserDetails user =
//				User.withDefaultPasswordEncoder()
//						.username("user1")
//						.password("secret123")
//						.roles("USER")
//						.build();
//
//		return new InMemoryUserDetailsManager(user);
//	}
	}
}
