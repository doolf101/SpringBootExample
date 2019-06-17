package com.doolf101.app.test.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(value = SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class SecurityConfigIntegrationTest {

	private TestRestTemplate restTemplate;
	private URL base;
	@LocalServerPort
	private int port;

	@Before
	public void setUp() throws MalformedURLException {
		base = new URL("http://localhost:" + port+"/page");
	}

	@Test
	public void whenLoggedUserRequestsHomePage_ThenSuccess() throws IllegalStateException {
		restTemplate = new TestRestTemplate("priya", "priya");
		ResponseEntity<String> response = restTemplate.getForEntity(base.toString(), String.class);
		System.out.println(response.getStatusCode());
		System.out.println(response.getBody());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().contains("Thymeleaf Example"));
	}

	@Test
	public void whenUserWithWrongCredentials_thenUnauthorizedPage() {
		restTemplate = new TestRestTemplate("user", "wrongpassword");
		ResponseEntity<String> response = restTemplate.getForEntity(base.toString(), String.class);
		System.out.println(response.getStatusCode());
		System.out.println(response.getBody());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().contains("Please sign in"));
	}

}