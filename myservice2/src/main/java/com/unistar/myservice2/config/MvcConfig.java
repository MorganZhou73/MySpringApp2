package com.unistar.myservice2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/home").setViewName("home"); // reference the view home.html
		registry.addViewController("/").setViewName("home2"); // this mapping has more priority than index.html

		// the mapping "/hello" in Greeting2Controller has more priority than hello.html here, so names /hello2
		registry.addViewController("/hello2").setViewName("hello"); // reference the view hello.html
		registry.addViewController("/login").setViewName("login");
	}

//	@Override
//	public void addCorsMappings(CorsRegistry registry) {
//		registry.addMapping("/greeting-javaconfig")
//				.allowedOrigins("http://localhost:8080")
//				.allowedMethods("POST", "GET",  "PUT", "OPTIONS", "DELETE")
//				.allowedHeaders("X-Auth-Token", "Content-Type", "Access-Control-Allow-Origin")
//				.exposedHeaders("Access-Control-Allow-Origin")
//				.allowCredentials(true).maxAge(3600);
//	}
}
