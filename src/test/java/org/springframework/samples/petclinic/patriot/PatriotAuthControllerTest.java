package org.springframework.samples.petclinic.patriot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedHashSet;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for the {@link PatriotAuthController}. Covers the registration form display,
 * registration form processing with validation, login form display, and login success
 * redirection.
 *
 * <p>
 * These tests use {@link WebMvcTest} to load only the web layer, with all dependencies
 * mocked via {@link MockitoBean}. The Turnstile CAPTCHA verification is bypassed by
 * using the Cloudflare test token value.
 * </p>
 *
 * @author Edward McKeown
 * @see PatriotAuthController
 */
@WebMvcTest(PatriotAuthController.class)
@DisabledInNativeImage
@DisabledInAotMode
class PatriotAuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private PatriotUserService patriotUserService;

	@MockitoBean
	private PatriotUserRepository patriotUserRepository;

	@MockitoBean(name = "patriotAuthenticationManager")
	private AuthenticationManager patriotAuthenticationManager;

	private PatriotUser testUser;

	private PatriotRole veteranRole;

	/**
	 * Sets up test data before each test method. Creates a test {@link PatriotUser}
	 * and {@link PatriotRole} with valid field values.
	 */
	@BeforeEach
	void setUp() {
		veteranRole = new PatriotRole();
		veteranRole.setId(1);
		veteranRole.setName("VETERAN");
		veteranRole.setDescription("Veteran user role");

		testUser = new PatriotUser();
		testUser.setId(1);
		testUser.setFirstName("John");
		testUser.setLastName("Doe");
		testUser.setEmail("john.doe@example.com");
		testUser.setPassword("Password1");
		testUser.setStatusId(1);
		LinkedHashSet<PatriotRole> roles = new LinkedHashSet<>();
		roles.add(veteranRole);
		testUser.setRoles(roles);
	}

	// ========================================================================
	// REGISTRATION - GET
	// ========================================================================

	/**
	 * Verifies that GET /patriot/register returns the registration form view with
	 * an empty {@code patriotUser} model attribute and the Turnstile site key.
	 */
	@Test
	@DisplayName("GET /patriot/register -> displays registration form")
	void testShowRegistrationForm() throws Exception {
		mockMvc.perform(get("/patriot/register"))
			.andExpect(status().isOk())
			.andExpect(view().name("patriot/auth/patriotRegisterForm"))
			.andExpect(model().attributeExists("patriotUser"))
			.andExpect(model().attributeExists("turnstileSiteKey"));
	}

	// ========================================================================
	// REGISTRATION - POST (Validation)
	// ========================================================================

	/**
	 * Verifies that submitting a valid registration form with all required fields
	 * saves the user, auto-logs them in, and redirects to the business listings
	 * with a welcome flash message.
	 */
	@Test
	@DisplayName("POST /patriot/register -> valid data redirects to /businesses")
	void testProcessRegistrationSuccess() throws Exception {
		// Mock the service to return the saved user
		given(patriotUserService.registerNewUser(any(PatriotUser.class))).willReturn(testUser);

		// Mock the authentication manager to return a valid token
		Authentication mockAuth = new UsernamePasswordAuthenticationToken(
			"john.doe@example.com", "Password1");
		given(patriotAuthenticationManager.authenticate(any())).willReturn(mockAuth);

		mockMvc.perform(post("/patriot/register")
				.param("firstName", "John")
				.param("lastName", "Doe")
				.param("email", "john.doe@example.com")
				.param("password", "Password1")
				.param("statusId", "1")
				.param("cf-turnstile-response", "test-token"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/businesses"))
			.andExpect(flash().attributeExists("messageSuccess"));

		verify(patriotUserService).registerNewUser(any(PatriotUser.class));
	}

	/**
	 * Verifies that submitting the registration form with a blank first name
	 * returns the form with validation errors.
	 */
	@Test
	@DisplayName("POST /patriot/register -> blank first name returns form with errors")
	void testProcessRegistrationBlankFirstName() throws Exception {
		mockMvc.perform(post("/patriot/register")
				.param("firstName", "")
				.param("lastName", "Doe")
				.param("email", "john.doe@example.com")
				.param("password", "Password1")
				.param("statusId", "1")
				.param("cf-turnstile-response", "test-token"))
			.andExpect(status().isOk())
			.andExpect(view().name("patriot/auth/patriotRegisterForm"))
			.andExpect(model().attributeHasFieldErrors("patriotUser", "firstName"));
	}

	/**
	 * Verifies that submitting the registration form with a blank last name
	 * returns the form with validation errors.
	 */
	@Test
	@DisplayName("POST /patriot/register -> blank last name returns form with errors")
	void testProcessRegistrationBlankLastName() throws Exception {
		mockMvc.perform(post("/patriot/register")
				.param("firstName", "John")
				.param("lastName", "")
				.param("email", "john.doe@example.com")
				.param("password", "Password1")
				.param("statusId", "1")
				.param("cf-turnstile-response", "test-token"))
			.andExpect(status().isOk())
			.andExpect(view().name("patriot/auth/patriotRegisterForm"))
			.andExpect(model().attributeHasFieldErrors("patriotUser", "lastName"));
	}

	/**
	 * Verifies that submitting the registration form with an invalid email
	 * returns the form with validation errors.
	 */
	@Test
	@DisplayName("POST /patriot/register -> invalid email returns form with errors")
	void testProcessRegistrationInvalidEmail() throws Exception {
		mockMvc.perform(post("/patriot/register")
				.param("firstName", "John")
				.param("lastName", "Doe")
				.param("email", "not-an-email")
				.param("password", "Password1")
				.param("statusId", "1")
				.param("cf-turnstile-response", "test-token"))
			.andExpect(status().isOk())
			.andExpect(view().name("patriot/auth/patriotRegisterForm"))
			.andExpect(model().attributeHasFieldErrors("patriotUser", "email"));
	}

	/**
	 * Verifies that submitting the registration form with a blank email
	 * returns the form with validation errors.
	 */
	@Test
	@DisplayName("POST /patriot/register -> blank email returns form with errors")
	void testProcessRegistrationBlankEmail() throws Exception {
		mockMvc.perform(post("/patriot/register")
				.param("firstName", "John")
				.param("lastName", "Doe")
				.param("email", "")
				.param("password", "Password1")
				.param("statusId", "1")
				.param("cf-turnstile-response", "test-token"))
			.andExpect(status().isOk())
			.andExpect(view().name("patriot/auth/patriotRegisterForm"))
			.andExpect(model().attributeHasFieldErrors("patriotUser", "email"));
	}

	/**
	 * Verifies that submitting the registration form with a weak password (no
	 * uppercase) returns the form with validation errors.
	 */
	@Test
	@DisplayName("POST /patriot/register -> weak password returns form with errors")
	void testProcessRegistrationWeakPassword() throws Exception {
		mockMvc.perform(post("/patriot/register")
				.param("firstName", "John")
				.param("lastName", "Doe")
				.param("email", "john.doe@example.com")
				.param("password", "weakpass")
				.param("statusId", "1")
				.param("cf-turnstile-response", "test-token"))
			.andExpect(status().isOk())
			.andExpect(view().name("patriot/auth/patriotRegisterForm"))
			.andExpect(model().attributeHasFieldErrors("patriotUser", "password"));
	}

	/**
	 * Verifies that submitting the registration form with a blank password
	 * returns the form with validation errors.
	 */
	@Test
	@DisplayName("POST /patriot/register -> blank password returns form with errors")
	void testProcessRegistrationBlankPassword() throws Exception {
		mockMvc.perform(post("/patriot/register")
				.param("firstName", "John")
				.param("lastName", "Doe")
				.param("email", "john.doe@example.com")
				.param("password", "")
				.param("statusId", "1")
				.param("cf-turnstile-response", "test-token"))
			.andExpect(status().isOk())
			.andExpect(view().name("patriot/auth/patriotRegisterForm"))
			.andExpect(model().attributeHasFieldErrors("patriotUser", "password"));
	}

	/**
	 * Verifies that a duplicate email during registration returns the form
	 * with a field-level error on the email field.
	 */
	@Test
	@DisplayName("POST /patriot/register -> duplicate email returns form with error")
	void testProcessRegistrationDuplicateEmail() throws Exception {
		// Mock the service to throw on duplicate
		given(patriotUserService.registerNewUser(any(PatriotUser.class)))
			.willThrow(new RuntimeException("Email already registered"));

		mockMvc.perform(post("/patriot/register")
				.param("firstName", "John")
				.param("lastName", "Doe")
				.param("email", "existing@example.com")
				.param("password", "Password1")
				.param("statusId", "1")
				.param("cf-turnstile-response", "test-token"))
			.andExpect(status().isOk())
			.andExpect(view().name("patriot/auth/patriotRegisterForm"))
			.andExpect(model().attributeHasFieldErrors("patriotUser", "email"));
	}

	/**
	 * Verifies that submitting the registration form without a Turnstile CAPTCHA
	 * token returns the form with a Turnstile error message.
	 */
	@Test
	@DisplayName("POST /patriot/register -> missing CAPTCHA returns form with error")
	void testProcessRegistrationMissingCaptcha() throws Exception {
		mockMvc.perform(post("/patriot/register")
				.param("firstName", "John")
				.param("lastName", "Doe")
				.param("email", "john.doe@example.com")
				.param("password", "Password1")
				.param("statusId", "1"))
			.andExpect(status().isOk())
			.andExpect(view().name("patriot/auth/patriotRegisterForm"))
			.andExpect(model().attributeExists("turnstileError"));
	}

	// ========================================================================
	// LOGIN - GET
	// ========================================================================

	/**
	 * Verifies that GET /patriot/login returns the login form view with an empty
	 * {@code patriotUser} model attribute and the Turnstile site key.
	 */
	@Test
	@DisplayName("GET /patriot/login -> displays login form")
	void testShowLoginForm() throws Exception {
		mockMvc.perform(get("/patriot/login"))
			.andExpect(status().isOk())
			.andExpect(view().name("patriot/auth/patriotLoginForm"))
			.andExpect(model().attributeExists("patriotUser"))
			.andExpect(model().attributeExists("turnstileSiteKey"));
	}

	// ========================================================================
	// LOGIN SUCCESS
	// ========================================================================

	/**
	 * Verifies that GET /patriot/login-success redirects authenticated users to
	 * the business listings with a welcome flash message.
	 */
	@Test
	@DisplayName("GET /patriot/login-success -> redirects to /businesses with flash message")
	void testProcessLoginSuccess() throws Exception {
		mockMvc.perform(get("/patriot/login-success")
				.principal(() -> "john.doe@example.com"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/businesses"))
			.andExpect(flash().attributeExists("messageSuccess"));
	}

	// ========================================================================
	// HOME PAGE
	// ========================================================================

	/**
	 * Verifies that GET /patriot returns the Patriot Thanks home page view.
	 */
	@Test
	@DisplayName("GET /patriot -> displays Patriot Thanks home page")
	void testShowHomePage() throws Exception {
		mockMvc.perform(get("/patriot"))
			.andExpect(status().isOk())
			.andExpect(view().name("patriot/patriotHome"));
	}

}
