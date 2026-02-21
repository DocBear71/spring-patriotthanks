/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.patriot.Business;
import org.springframework.samples.petclinic.patriot.BusinessRepository;
import org.springframework.samples.petclinic.patriot.BusinessType;
import org.springframework.samples.petclinic.patriot.BusinessTypeRepository;
import org.springframework.samples.petclinic.patriot.Incentive;
import org.springframework.samples.petclinic.patriot.IncentiveRepository;
import org.springframework.samples.petclinic.school.SchoolRepository;
import org.springframework.samples.petclinic.user.User;
import org.springframework.samples.petclinic.user.UserRepository;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Integration tests running against a real MySQL database via Testcontainers. Verifies
 * that the repository layer correctly interacts with the database schema and seed data
 * defined in {@code schema.sql} and {@code data.sql}, and that key HTTP endpoints return
 * the expected responses.
 *
 * @author Edward McKeown
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("mysql")
@Testcontainers
class MySqlIntegrationTests {

	@ServiceConnection
	@Container
	static MySQLContainer container = new MySQLContainer(DockerImageName.parse("mysql:9.5"));

	@LocalServerPort
	int port;

	@Autowired
	private VetRepository vets;

	@Autowired
	private UserRepository users;

	@Autowired
	private SchoolRepository schools;

	@Autowired
	private BusinessRepository businesses;

	@Autowired
	private BusinessTypeRepository businessTypes;

	@Autowired
	private IncentiveRepository incentives;

	@Autowired
	private RestTemplateBuilder builder;

	// =========================================================================
	// Existing Tests
	// =========================================================================

	/**
	 * Verifies that the findAll methods on vets, users, and schools repositories return
	 * non-empty results and that cached calls also succeed.
	 */
	@Test
	void testFindAll() {
		vets.findAll();
		vets.findAll(); // served from cache
		assertThat(vets.findAll()).isNotEmpty();
		users.findAll();
		users.findAll(); // served from cache
		assertThat(users.findAll()).isNotEmpty();
		schools.findAll();
		schools.findAll(); // served from cache
		assertThat(schools.findAll()).isNotEmpty();
	}

	/**
	 * Verifies that the school details endpoint returns a 200 OK response containing the
	 * expected school name when accessed via the domain slug.
	 */
	@Test
	void testSchoolDetails() {
		RestTemplate template = builder.rootUri("http://localhost:" + port).build();
		ResponseEntity<String> result = template.exchange(RequestEntity.get("/schools/kirkwood").build(),
			String.class);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).contains("Kirkwood Community College");
	}

	// =========================================================================
	// User Tests
	// =========================================================================

	/**
	 * Verifies that a user can be found by email address using the seed data.
	 */
	@Test
	@DisplayName("Should find user by email")
	void testFindUserByEmail() {
		Optional<User> optionalUser = users.findByEmail("edward@doctormckeown.com");
		assertThat(optionalUser).isPresent();
		User user = optionalUser.get();
		assertThat(user.getFirstName()).isEqualTo("Edward");
		assertThat(user.getLastName()).isEqualTo("McKeown");
	}

	/**
	 * Verifies that searching for a non-existent email returns empty.
	 */
	@Test
	@DisplayName("Should return empty for unknown email")
	void testFindUserByEmailNotFound() {
		Optional<User> optionalUser = users.findByEmail("nobody@example.com");
		assertThat(optionalUser).isEmpty();
	}

	/**
	 * Verifies that a user can be retrieved by ID and has the expected email.
	 */
	@Test
	@DisplayName("Should find user by ID")
	void testFindUserById() {
		Optional<User> optionalUser = users.findById(1);
		assertThat(optionalUser).isPresent();
		assertThat(optionalUser.get().getEmail()).isEqualTo("edward@doctormckeown.com");
	}

	// =========================================================================
	// Business Tests
	// =========================================================================

	/**
	 * Verifies that the findAll method on the business repository returns non-empty
	 * results and that cached calls also succeed.
	 */
	@Test
	@DisplayName("Should find all businesses")
	void testFindAllBusinesses() {
		businesses.findAll();
		businesses.findAll(); // served from cache
		assertThat(businesses.findAll()).isNotEmpty();
	}

	/**
	 * Verifies that a single business can be found by ID with its associated business
	 * type loaded.
	 */
	@Test
	@DisplayName("Should find business by ID with business type")
	void testFindBusinessById() {
		Optional<Business> optionalBusiness = businesses.findById(1);
		assertThat(optionalBusiness).isPresent();
		Business business = optionalBusiness.get();
		assertThat(business.getName()).isEqualTo("Veterans Auto Service");
		assertThat(business.getBusinessType()).isNotNull();
		assertThat(business.getBusinessType().getName()).isEqualTo("Automotive");
	}

	/**
	 * Verifies that a business can be retrieved with all associated locations and
	 * incentives eagerly loaded via the {@code findByIdWithDetails} query.
	 */
	@Test
	@DisplayName("Should find business by ID with locations and incentives")
	void testFindBusinessByIdWithDetails() {
		Optional<Business> optionalBusiness = businesses.findByIdWithDetails(1);
		assertThat(optionalBusiness).isPresent();
		Business business = optionalBusiness.get();
		assertThat(business.getName()).isEqualTo("Veterans Auto Service");
		assertThat(business.getLocations()).isNotEmpty();
		assertThat(business.getIncentives()).isNotEmpty();
	}

	/**
	 * Verifies that all business types are returned from the database and that the first
	 * type (by display order) is Restaurant.
	 */
	@Test
	@DisplayName("Should find all business types")
	void testFindAllBusinessTypes() {
		Collection<BusinessType> types = businessTypes.findAllByOrderByDisplayOrderAsc();
		assertThat(types).isNotEmpty();
		assertThat(types.size()).isGreaterThanOrEqualTo(8);
		assertThat(types.iterator().next().getName()).isEqualTo("Restaurant");
	}

	/**
	 * Verifies that active incentives with eagerly loaded incentive types can be
	 * retrieved for a specific business.
	 */
	@Test
	@DisplayName("Should find active incentives for a business with incentive types")
	void testFindActiveIncentivesForBusiness() {
		var activeIncentives = incentives.findByBusinessIdAndIsActive(1, true);
		assertThat(activeIncentives).isNotEmpty();
		Incentive incentive = activeIncentives.get(0);
		assertThat(incentive.getTitle()).isEqualTo("10% Military Discount");
		assertThat(incentive.getIncentiveTypes()).isNotEmpty();
	}

	/**
	 * Verifies that the business details endpoint returns a 200 OK response containing
	 * the expected business name when accessed via the numeric ID.
	 */
	@Test
	@DisplayName("GET /businesses/1 -> returns business details page")
	void testBusinessDetails() {
		RestTemplate template = builder.rootUri("http://localhost:" + port).build();
		ResponseEntity<String> result = template.exchange(RequestEntity.get("/businesses/1").build(), String.class);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).contains("Veterans Auto Service");
	}

}
