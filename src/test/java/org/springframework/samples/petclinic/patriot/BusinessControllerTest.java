package org.springframework.samples.petclinic.patriot;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for the {@link BusinessController}. Tests the business listing functionality
 * with pagination and the incentives AJAX endpoint.
 *
 * @author Edward McKeown
 */
@WebMvcTest(BusinessController.class)
@DisabledInNativeImage
@DisabledInAotMode
class BusinessControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private BusinessRepository businessRepository;

	@MockitoBean
	private BusinessTypeRepository businessTypeRepository;

	@MockitoBean
	private IncentiveRepository incentiveRepository;

	private Business business1;

	private Business business2;

	private Business business3;

	private BusinessType restaurantType;

	private BusinessType retailType;

	private Incentive incentive1;

	private Incentive incentive2;

	private IncentiveType veteranType;

	private IncentiveType activeDutyType;

	@BeforeEach
	void setUp() {
		// Create test business types
		restaurantType = new BusinessType();
		restaurantType.setId(1);
		restaurantType.setName("Restaurant");
		restaurantType.setDescription("Food and dining services");
		restaurantType.setDisplayOrder(1);

		retailType = new BusinessType();
		retailType.setId(2);
		retailType.setName("Retail");
		retailType.setDescription("General retail goods");
		retailType.setDisplayOrder(2);

		given(businessTypeRepository.findById(1)).willReturn(restaurantType);
		given(businessTypeRepository.findById(2)).willReturn(retailType);

		// Create test businesses
		business1 = new Business();
		business1.setId(1);
		business1.setName("Joe's Pizza");
		business1.setBusinessType(restaurantType);
		business1.setWebsite("https://joespizza.com");
		business1.setIsActive(true);
		business1.setIsVerified(true);

		business2 = new Business();
		business2.setId(2);
		business2.setName("Main Street Hardware");
		business2.setBusinessType(retailType);
		business2.setWebsite("https://mainstreethardware.com");
		business2.setIsActive(true);
		business2.setIsVerified(false);

		business3 = new Business();
		business3.setId(3);
		business3.setName("Cedar Rapids Diner");
		business3.setBusinessType(restaurantType);
		business3.setIsActive(true);
		business3.setIsVerified(true);

		// Create test incentive types
		veteranType = new IncentiveType();
		veteranType.setId(1);
		veteranType.setName("Veteran");
		veteranType.setDescription("Veterans special pricing");
		veteranType.setDisplayOrder(1);
		veteranType.setIsActive(true);

		activeDutyType = new IncentiveType();
		activeDutyType.setId(2);
		activeDutyType.setName("Active Duty");
		activeDutyType.setDescription("Active duty military discount");
		activeDutyType.setDisplayOrder(2);
		activeDutyType.setIsActive(true);

		// Create test incentives
		incentive1 = new Incentive();
		incentive1.setId(1);
		incentive1.setTitle("10% Discount for Veterans");
		incentive1.setDescription("Show valid military ID for discount");
		incentive1.setDiscountPercentage(new BigDecimal("10.00"));
		incentive1.setVerificationRequired("Military ID or DD214");
		incentive1.setStartDate(LocalDate.of(2024, 1, 1));
		incentive1.setEndDate(LocalDate.of(2026, 12, 31));
		incentive1.setIsActive(true);
		incentive1.setBusiness(business1);
		incentive1.getIncentiveTypes().add(veteranType);

		incentive2 = new Incentive();
		incentive2.setId(2);
		incentive2.setTitle("15% Active Duty Discount");
		incentive2.setDescription("Active duty personnel receive 15% off");
		incentive2.setDiscountPercentage(new BigDecimal("15.00"));
		incentive2.setVerificationRequired("Active military ID");
		incentive2.setStartDate(LocalDate.of(2024, 1, 1));
		incentive2.setEndDate(LocalDate.of(2026, 12, 31));
		incentive2.setIsActive(true);
		incentive2.setBusiness(business1);
		incentive2.getIncentiveTypes().add(activeDutyType);
	}

	@Test
	void testShowBusinessList() throws Exception {
		// Given
		List<Business> businesses = List.of(business1, business2, business3);
		Page<Business> businessPage = new PageImpl<>(businesses, PageRequest.of(0, 5), businesses.size());

		given(businessRepository.findAll(any(Pageable.class))).willReturn(businessPage);

		// When & Then
		mockMvc.perform(get("/businesses"))
			.andExpect(status().isOk())
			.andExpect(view().name("businesses/businessList"))
			.andExpect(model().attributeExists("listBusinesses"))
			.andExpect(model().attribute("listBusinesses", hasSize(3)))
			.andExpect(model().attribute("currentPage", 1))
			.andExpect(model().attribute("totalPages", 1))
			.andExpect(model().attribute("totalItems", 3L));
	}

	@Test
	void testShowBusinessListWithPagination() throws Exception {
		// Given - 7 businesses across 2 pages (5 per page)
		List<Business> allBusinesses = new ArrayList<>();
		for (int i = 1; i <= 7; i++) {
			Business b = new Business();
			b.setId(i);
			b.setName("Business " + i);
			b.setBusinessType(restaurantType);
			b.setIsActive(true);
			allBusinesses.add(b);
		}

		// Page 2 should have 2 businesses
		List<Business> page2Businesses = allBusinesses.subList(5, 7);
		Page<Business> businessPage = new PageImpl<>(page2Businesses, PageRequest.of(1, 5), allBusinesses.size());

		given(businessRepository.findAll(any(Pageable.class))).willReturn(businessPage);

		// When & Then
		mockMvc.perform(get("/businesses").param("page", "2"))
			.andExpect(status().isOk())
			.andExpect(view().name("businesses/businessList"))
			.andExpect(model().attribute("listBusinesses", hasSize(2)))
			.andExpect(model().attribute("currentPage", 2))
			.andExpect(model().attribute("totalPages", 2))
			.andExpect(model().attribute("totalItems", 7L));
	}

	@Test
	void testShowBusinessListEmpty() throws Exception {
		// Given
		Page<Business> emptyPage = new PageImpl<>(List.of(), PageRequest.of(0, 5), 0);
		given(businessRepository.findAll(any(Pageable.class))).willReturn(emptyPage);

		// When & Then
		mockMvc.perform(get("/businesses"))
			.andExpect(status().isOk())
			.andExpect(view().name("businesses/businessList"))
			.andExpect(model().attribute("listBusinesses", hasSize(0)))
			.andExpect(model().attribute("totalItems", 0L));
	}

	@Test
	void testBusinessListContainsBusinessNames() throws Exception {
		// Given
		List<Business> businesses = List.of(business1, business2);
		Page<Business> businessPage = new PageImpl<>(businesses, PageRequest.of(0, 5), businesses.size());

		given(businessRepository.findAll(any(Pageable.class))).willReturn(businessPage);

		// When & Then
		mockMvc.perform(get("/businesses"))
			.andExpect(status().isOk())
			.andExpect(model().attribute("listBusinesses", hasItem(hasProperty("name", is("Joe's Pizza")))))
			.andExpect(model().attribute("listBusinesses", hasItem(hasProperty("name", is("Main Street Hardware")))));
	}

	@Test
	void testBusinessListContainsBusinessTypes() throws Exception {
		// Given
		List<Business> businesses = List.of(business1, business3);
		Page<Business> businessPage = new PageImpl<>(businesses, PageRequest.of(0, 5), businesses.size());

		given(businessRepository.findAll(any(Pageable.class))).willReturn(businessPage);

		// When & Then
		mockMvc.perform(get("/businesses"))
			.andExpect(status().isOk())
			.andExpect(model().attribute("listBusinesses",
					hasItem(hasProperty("businessType", hasProperty("name", is("Restaurant"))))));
	}

	@Test
	void testBusinessListDefaultPage() throws Exception {
		// Given
		List<Business> businesses = List.of(business1);
		Page<Business> businessPage = new PageImpl<>(businesses, PageRequest.of(0, 5), businesses.size());

		given(businessRepository.findAll(any(Pageable.class))).willReturn(businessPage);

		// When - no page parameter provided
		// Then - should default to page 1
		mockMvc.perform(get("/businesses")).andExpect(status().isOk()).andExpect(model().attribute("currentPage", 1));
	}

	@Test
	void testGetBusinessIncentives() throws Exception {
		// Given
		List<Incentive> incentives = List.of(incentive1, incentive2);
		given(incentiveRepository.findByBusinessIdAndIsActive(1, true)).willReturn(incentives);

		// When & Then
		mockMvc.perform(get("/businesses/1/incentives").contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$", hasSize(2)))
			.andExpect(jsonPath("$[0].id", is(1)))
			.andExpect(jsonPath("$[0].title", is("10% Discount for Veterans")))
			.andExpect(jsonPath("$[0].description", is("Show valid military ID for discount")))
			.andExpect(jsonPath("$[0].discountPercentage", is(10.0)))
			.andExpect(jsonPath("$[0].verificationRequired", is("Military ID or DD214")))
			.andExpect(jsonPath("$[0].formattedDiscount", is("10%")))
			.andExpect(jsonPath("$[0].incentiveTypes", hasSize(1)))
			.andExpect(jsonPath("$[0].incentiveTypes[0].name", is("Veteran")))
			.andExpect(jsonPath("$[1].id", is(2)))
			.andExpect(jsonPath("$[1].title", is("15% Active Duty Discount")))
			.andExpect(jsonPath("$[1].discountPercentage", is(15.0)))
			.andExpect(jsonPath("$[1].incentiveTypes[0].name", is("Active Duty")));
	}

	@Test
	void testGetBusinessIncentivesEmpty() throws Exception {
		// Given
		given(incentiveRepository.findByBusinessIdAndIsActive(2, true)).willReturn(List.of());

		// When & Then
		mockMvc.perform(get("/businesses/2/incentives").contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	void testGetBusinessIncentivesWithMultipleTypes() throws Exception {
		// Given - Create incentive with multiple types
		Incentive multiTypeIncentive = new Incentive();
		multiTypeIncentive.setId(3);
		multiTypeIncentive.setTitle("20% Military and First Responder Discount");
		multiTypeIncentive.setDescription("For all service members and first responders");
		multiTypeIncentive.setDiscountPercentage(new BigDecimal("20.00"));
		multiTypeIncentive.setIsActive(true);
		multiTypeIncentive.setBusiness(business1);
		multiTypeIncentive.getIncentiveTypes().add(veteranType);
		multiTypeIncentive.getIncentiveTypes().add(activeDutyType);

		given(incentiveRepository.findByBusinessIdAndIsActive(1, true)).willReturn(List.of(multiTypeIncentive));

		// When & Then
		mockMvc.perform(get("/businesses/1/incentives").contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(1)))
			.andExpect(jsonPath("$[0].incentiveTypes", hasSize(2)))
			.andExpect(jsonPath("$[0].incentiveTypes[*].name", containsInAnyOrder("Veteran", "Active Duty")));
	}

	@Test
	void testGetBusinessIncentivesReturnsOnlyActiveIncentives() throws Exception {
		// Given
		List<Incentive> activeIncentives = List.of(incentive1);
		given(incentiveRepository.findByBusinessIdAndIsActive(1, true)).willReturn(activeIncentives);

		// When & Then - should only return active incentives
		mockMvc.perform(get("/businesses/1/incentives"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(1)))
			.andExpect(jsonPath("$[0].id", is(1)));
	}

	@Test
	void testGetBusinessIncentivesFormattedDiscount() throws Exception {
		// Given - Test different discount formats
		Incentive percentageIncentive = new Incentive();
		percentageIncentive.setId(4);
		percentageIncentive.setTitle("25% Discount");
		percentageIncentive.setDescription("Test");
		percentageIncentive.setDiscountPercentage(new BigDecimal("25.00"));
		percentageIncentive.setIsActive(true);
		percentageIncentive.setBusiness(business1);

		Incentive dollarIncentive = new Incentive();
		dollarIncentive.setId(5);
		dollarIncentive.setTitle("$5 Off");
		dollarIncentive.setDescription("Test");
		dollarIncentive.setDiscountAmount(new BigDecimal("5.00"));
		dollarIncentive.setIsActive(true);
		dollarIncentive.setBusiness(business1);

		given(incentiveRepository.findByBusinessIdAndIsActive(1, true))
			.willReturn(List.of(percentageIncentive, dollarIncentive));

		// When & Then
		mockMvc.perform(get("/businesses/1/incentives"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].formattedDiscount", is("25%")))
			.andExpect(jsonPath("$[1].formattedDiscount", is("$5.00 off")));
	}

	@Test
	@DisplayName("User clicks \"Add Business\" -> GET /businesses/new")
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/businesses/new"))
			.andExpect(status().isOk())
			.andExpect(view().name("businesses/createOrUpdateBusinessForm"))
			.andExpect(model().attributeExists("business"));
	}

	@Test
	@DisplayName("Validation Passed -> verify save() is called and redirect to /businesses")
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc
			.perform(post("/businesses/new").param("name", "Patriot Burgers")
				.param("businessType.id", "1")
				.param("website", "https://patriotburgers.com")
				.param("description", "Great burgers for heroes"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/businesses"));

		// Verify that the repository.save() method was actually called
		verify(businessRepository).save(any(Business.class));
	}

	@Test
	@DisplayName("Validation Failed -> send a blank name and ensure the form is returned with errors")
	void testProcessCreationFormHasErrorsBlankName() throws Exception {
		mockMvc.perform(post("/businesses/new").param("name", "") // Empty name should
			// trigger @NotBlank
			.param("businessType.id", "1"))
			.andExpect(status().isOk()) // 200 OK because we are re-rendering the form
			.andExpect(model().attributeHasErrors("business"))
			.andExpect(model().attributeHasFieldErrors("business", "name"))
			.andExpect(view().name("businesses/createOrUpdateBusinessForm"));
	}

	@Test
	@DisplayName("Validation Failed -> no business type selected returns form with errors")
	void testProcessCreationFormHasErrorsNoBusinessType() throws Exception {
		mockMvc.perform(post("/businesses/new").param("name", "Good Business"))
			// No businessType parameter — should trigger @NotNull
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("business"))
			.andExpect(model().attributeHasFieldErrors("business", "businessType"))
			.andExpect(view().name("businesses/createOrUpdateBusinessForm"));
	}

	@Test
	@DisplayName("GET /businesses/{id} -> returns business details view")
	void testShowBusinessDetails() throws Exception {
		given(businessRepository.findByIdWithDetails(1)).willReturn(java.util.Optional.of(business1));

		mockMvc.perform(get("/businesses/1"))
			.andExpect(status().isOk())
			.andExpect(view().name("businesses/businessDetails"))
			.andExpect(model().attributeExists("business"))
			.andExpect(model().attribute("business", hasProperty("name", is("Joe's Pizza"))));
	}

	@Test
	@DisplayName("GET /businesses/{id} -> returns 404 when business not found")
	void testShowBusinessDetailsNotFound() throws Exception {
		given(businessRepository.findByIdWithDetails(999)).willReturn(java.util.Optional.empty());

		mockMvc.perform(get("/businesses/999")).andExpect(status().isNotFound());
	}

}
