package com.ecznt.productservice;

import com.ecznt.productservice.dto.ProductRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	@Container
	static MySQLContainer mySQLContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.0.32"));

	@Autowired
	private MockMvc mockMvc;

	private ObjectMapper objectMapper;


	@DynamicPropertySource
	static void mysqlProperties(DynamicPropertyRegistry registry){
		registry.add("spring.datasource.url", () -> mySQLContainer.getJdbcUrl());
		registry.add("spring.datasource.driverClassName", () -> mySQLContainer.getDriverClassName());
		registry.add("spring.datasource.username", () -> mySQLContainer.getUsername());
		registry.add("spring.datasource.password", () -> mySQLContainer.getPassword());
		registry.add("spring.flyway.enabled", () -> "true");
	}


	@Test
	void shouldCreateProduct() throws Exception {

		ProductRequest productRequest = getProductRequest();
		String productRequestString = objectMapper.writeValueAsString(productRequest);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
				.contentType(MediaType.APPLICATION_JSON)
				.content(productRequestString))
				.andExpect(status().isCreated());
	}

	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
				.name("İphone 13")
				.description("İphone 13")
				.price(BigDecimal.valueOf(1200))
				.build();
	}
}
