package com.unistar.myservice1;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// if don't need the DataSource, add
// @EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
// otherwise, UnsatisfiedDependencyException would be thrown:
// Error creating bean with name 'org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaConfiguration'
@SpringBootTest
class Myservice1ApplicationTests {

	@Test
	void contextLoads() {
	}
}
