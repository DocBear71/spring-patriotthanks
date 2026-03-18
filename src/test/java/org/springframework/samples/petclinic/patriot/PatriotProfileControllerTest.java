package org.springframework.samples.petclinic.patriot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for {@link PatriotProfileController}. Covers the profile GET display,
 * profile POST update with all validation paths, Gravatar URL generation, zip code
 * handling, and soft account deletion.
 *
 * <p>
 * All tests supply the authenticated principal directly on each MockMvc request using
 * the lambda form {@code .principal(() -> TEST_EMAIL)}, matching the pattern established
 * in {@link PatriotAuthControllerTest}. This approach works correctly with
 * {@link WebMvcTest} without needing to import the full security filter chain.
 * </p>
 *
 * <p>
 * {@code @Valid} is intentionally absent from the POST handler (to avoid password
 * re-validation on profile updates), so manual validation logic in the controller is
 * exercised directly by each test.
 * </p>
 *
 * @author Edward McKeown
 * @see PatriotProfileController
 * @see PatriotUser
 */
@WebMvcTest(PatriotProfileController.class)
@DisabledInNativeImage
@DisabledInAotMode
class PatriotProfileControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private PatriotUserRepository patriotUserRepository;

	@MockitoBean
	private PasswordEncoder passwordEncoder;

	@MockitoBean(name = "patriotUserDetailsService")
	private UserDetailsService patriotUserDetailsService;

	private PatriotUser testUser;

	/** The email address used as the mock principal in every test. */
	private static final String TEST_EMAIL = "john.doe@example.com";

	/**
	 * Creates a fully-populated {@link PatriotUser} fixture before each test and
	 * configures the repository mock to return it whenever the test email is looked up.
	 */
	@BeforeEach
	void setUp() {
		PatriotRole veteranRole = new PatriotRole();
		veteranRole.setId(1);
		veteranRole.setName("VETERAN");

		testUser = new PatriotUser();
		testUser.setId(1);
		testUser.setFirstName("John");
		testUser.setLastName("Doe");
		testUser.setEmail(TEST_EMAIL);
		testUser.setPassword("$2a$10$hashedpassword");
		testUser.setPhone("3195550123");
		testUser.setStatusId(1);
		testUser.setZipCode("52404");
		testUser.setAvatarUrl("https://www.gravatar.com/avatar/abc123?d=identicon&s=200");
		LinkedHashSet<PatriotRole> roles = new LinkedHashSet<>();
		roles.add(veteranRole);
		testUser.setRoles(roles);

		given(patriotUserRepository.findByEmail(TEST_EMAIL)).willReturn(Optional.of(testUser));
		given(patriotUserRepository.save(any(PatriotUser.class))).willReturn(testUser);
	}

	// ========================================================================
	// PROFILE - GET
	// ========================================================================

	/**
	 * Verifies that GET /patriot/profile returns the profile view with the
	 * {@code patriotUser} model attribute populated from the repository.
	 */
	@Test
	@DisplayName("GET /patriot/profile -> displays profile form with user data")
	void testShowProfileForm() throws Exception {
		mockMvc.perform(get("/patriot/profile").principal(() -> TEST_EMAIL))
			.andExpect(status().isOk())
			.andExpect(view().name("patriot/patriotProfile"))
			.andExpect(model().attributeExists("patriotUser"));

		verify(patriotUserRepository).findByEmail(TEST_EMAIL);
	}

	/**
	 * Verifies that the phone number is formatted from the raw 10-digit database format
	 * to {@code (XXX) XXX-XXXX} before being placed in the model.
	 */
	@Test
	@DisplayName("GET /patriot/profile -> formats 10-digit phone number for display")
	void testShowProfileFormFormatsPhone() throws Exception {
		mockMvc.perform(get("/patriot/profile").principal(() -> TEST_EMAIL))
			.andExpect(status().isOk())
			.andExpect(model().attribute("patriotUser",
				org.hamcrest.Matchers.hasProperty("phone",
					org.hamcrest.Matchers.equalTo("(319) 555-0123"))));
	}

	/**
	 * Verifies that when a user has no {@code avatarUrl}, the controller generates a
	 * Gravatar URL from the email and saves it before rendering the form.
	 */
	@Test
	@DisplayName("GET /patriot/profile -> seeds Gravatar URL when avatarUrl is null")
	void testShowProfileFormSeedsGravatar() throws Exception {
		testUser.setAvatarUrl(null);

		mockMvc.perform(get("/patriot/profile").principal(() -> TEST_EMAIL))
			.andExpect(status().isOk())
			.andExpect(view().name("patriot/patriotProfile"));

		verify(patriotUserRepository, atLeastOnce()).save(any(PatriotUser.class));
	}

	// ========================================================================
	// PROFILE - POST (Happy Path)
	// ========================================================================

	/**
	 * Verifies that a valid profile update saves the user and redirects to the profile
	 * page with a success flash message.
	 */
	@Test
	@DisplayName("POST /patriot/profile -> valid data saves and redirects with flash")
	void testProcessProfileUpdateSuccess() throws Exception {
		mockMvc
			.perform(post("/patriot/profile").principal(() -> TEST_EMAIL)
				.param("firstName", "John")
				.param("lastName", "Doe")
				.param("email", TEST_EMAIL)
				.param("phone", "(319) 555-0123")
				.param("password", "")
				.param("statusId", "1")
				.param("zipCode", "52404"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/patriot/profile"))
			.andExpect(flash().attributeExists("messageSuccess"));

		verify(patriotUserRepository).save(any(PatriotUser.class));
	}

	/**
	 * Verifies that a valid profile update with a new password hashes it and saves.
	 */
	@Test
	@DisplayName("POST /patriot/profile -> valid new password is encoded and saved")
	void testProcessProfileUpdateWithPasswordChange() throws Exception {
		given(passwordEncoder.encode("NewPass1")).willReturn("$2a$10$newhash");

		mockMvc
			.perform(post("/patriot/profile").principal(() -> TEST_EMAIL)
				.param("firstName", "John")
				.param("lastName", "Doe")
				.param("email", TEST_EMAIL)
				.param("password", "NewPass1")
				.param("statusId", "1")
				.param("zipCode", "52404"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/patriot/profile"));

		verify(passwordEncoder).encode("NewPass1");
		verify(patriotUserRepository).save(any(PatriotUser.class));
	}

	/**
	 * Verifies that leaving the password field blank skips password encoding and saves
	 * the existing hashed password unchanged.
	 */
	@Test
	@DisplayName("POST /patriot/profile -> blank password skips encoding")
	void testProcessProfileUpdateBlankPasswordSkipsEncoding() throws Exception {
		mockMvc
			.perform(post("/patriot/profile").principal(() -> TEST_EMAIL)
				.param("firstName", "John")
				.param("lastName", "Doe")
				.param("email", TEST_EMAIL)
				.param("password", "")
				.param("statusId", "1"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/patriot/profile"));

		verify(passwordEncoder, never()).encode(anyString());
	}

	// ========================================================================
	// PROFILE - POST (Gravatar regeneration on email change)
	// ========================================================================

	/**
	 * Verifies that when the user changes their email address, the controller
	 * regenerates the Gravatar URL, refreshes the Spring Security context, and
	 * redirects successfully.
	 */
	@Test
	@DisplayName("POST /patriot/profile -> email change triggers Gravatar regeneration")
	void testProcessProfileUpdateEmailChangeRegeneratesGravatar() throws Exception {
		final String newEmail = "new.email@example.com";

		given(patriotUserRepository.existsByEmail(newEmail)).willReturn(false);

		UserDetails mockDetails = mock(UserDetails.class);
		given(mockDetails.getAuthorities()).willReturn(Collections.emptyList());
		given(patriotUserDetailsService.loadUserByUsername(newEmail)).willReturn(mockDetails);

		mockMvc
			.perform(post("/patriot/profile").principal(() -> TEST_EMAIL)
				.param("firstName", "John")
				.param("lastName", "Doe")
				.param("email", newEmail)
				.param("password", "")
				.param("statusId", "1"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/patriot/profile"));

		verify(patriotUserDetailsService).loadUserByUsername(newEmail);
		verify(patriotUserRepository).save(any(PatriotUser.class));
	}

	// ========================================================================
	// PROFILE - POST (Zip Code)
	// ========================================================================

	/**
	 * Verifies that a valid 5-digit zip code is accepted and saved.
	 */
	@Test
	@DisplayName("POST /patriot/profile -> valid zip code is accepted")
	void testProcessProfileUpdateValidZipCode() throws Exception {
		mockMvc
			.perform(post("/patriot/profile").principal(() -> TEST_EMAIL)
				.param("firstName", "John")
				.param("lastName", "Doe")
				.param("email", TEST_EMAIL)
				.param("password", "")
				.param("statusId", "1")
				.param("zipCode", "52404"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/patriot/profile"));
	}

	/**
	 * Verifies that an invalid zip code (fewer than 5 digits) returns the form with a
	 * field-level error on {@code zipCode}.
	 */
	@Test
	@DisplayName("POST /patriot/profile -> invalid zip code returns form with error")
	void testProcessProfileUpdateInvalidZipCode() throws Exception {
		mockMvc
			.perform(post("/patriot/profile").principal(() -> TEST_EMAIL)
				.param("firstName", "John")
				.param("lastName", "Doe")
				.param("email", TEST_EMAIL)
				.param("password", "")
				.param("statusId", "1")
				.param("zipCode", "1234"))
			.andExpect(status().isOk())
			.andExpect(view().name("patriot/patriotProfile"))
			.andExpect(model().attributeHasFieldErrors("patriotUser", "zipCode"));
	}

	/**
	 * Verifies that a blank zip code clears the field without error.
	 */
	@Test
	@DisplayName("POST /patriot/profile -> blank zip code clears the field without error")
	void testProcessProfileUpdateBlankZipCodeClearsField() throws Exception {
		mockMvc
			.perform(post("/patriot/profile").principal(() -> TEST_EMAIL)
				.param("firstName", "John")
				.param("lastName", "Doe")
				.param("email", TEST_EMAIL)
				.param("password", "")
				.param("statusId", "1")
				.param("zipCode", ""))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/patriot/profile"));
	}

	// ========================================================================
	// PROFILE - POST (Validation Errors)
	// ========================================================================

	/**
	 * Verifies that a blank first name returns the profile form with a field-level error.
	 */
	@Test
	@DisplayName("POST /patriot/profile -> blank first name returns form with error")
	void testProcessProfileUpdateBlankFirstName() throws Exception {
		mockMvc
			.perform(post("/patriot/profile").principal(() -> TEST_EMAIL)
				.param("firstName", "")
				.param("lastName", "Doe")
				.param("email", TEST_EMAIL)
				.param("password", "")
				.param("statusId", "1"))
			.andExpect(status().isOk())
			.andExpect(view().name("patriot/patriotProfile"))
			.andExpect(model().attributeHasFieldErrors("patriotUser", "firstName"));
	}

	/**
	 * Verifies that a blank last name returns the profile form with a field-level error.
	 */
	@Test
	@DisplayName("POST /patriot/profile -> blank last name returns form with error")
	void testProcessProfileUpdateBlankLastName() throws Exception {
		mockMvc
			.perform(post("/patriot/profile").principal(() -> TEST_EMAIL)
				.param("firstName", "John")
				.param("lastName", "")
				.param("email", TEST_EMAIL)
				.param("password", "")
				.param("statusId", "1"))
			.andExpect(status().isOk())
			.andExpect(view().name("patriot/patriotProfile"))
			.andExpect(model().attributeHasFieldErrors("patriotUser", "lastName"));
	}

	/**
	 * Verifies that a blank email returns the profile form with a field-level error.
	 */
	@Test
	@DisplayName("POST /patriot/profile -> blank email returns form with error")
	void testProcessProfileUpdateBlankEmail() throws Exception {
		mockMvc
			.perform(post("/patriot/profile").principal(() -> TEST_EMAIL)
				.param("firstName", "John")
				.param("lastName", "Doe")
				.param("email", "")
				.param("password", "")
				.param("statusId", "1"))
			.andExpect(status().isOk())
			.andExpect(view().name("patriot/patriotProfile"))
			.andExpect(model().attributeHasFieldErrors("patriotUser", "email"));
	}

	/**
	 * Verifies that a malformed email address returns the profile form with a field-level
	 * error.
	 */
	@Test
	@DisplayName("POST /patriot/profile -> invalid email format returns form with error")
	void testProcessProfileUpdateInvalidEmail() throws Exception {
		mockMvc
			.perform(post("/patriot/profile").principal(() -> TEST_EMAIL)
				.param("firstName", "John")
				.param("lastName", "Doe")
				.param("email", "not-an-email")
				.param("password", "")
				.param("statusId", "1"))
			.andExpect(status().isOk())
			.andExpect(view().name("patriot/patriotProfile"))
			.andExpect(model().attributeHasFieldErrors("patriotUser", "email"));
	}

	/**
	 * Verifies that attempting to change to an email already registered to another
	 * account returns the profile form with a duplicate-email field error.
	 */
	@Test
	@DisplayName("POST /patriot/profile -> duplicate email returns form with error")
	void testProcessProfileUpdateDuplicateEmail() throws Exception {
		given(patriotUserRepository.existsByEmail("taken@example.com")).willReturn(true);

		mockMvc
			.perform(post("/patriot/profile").principal(() -> TEST_EMAIL)
				.param("firstName", "John")
				.param("lastName", "Doe")
				.param("email", "taken@example.com")
				.param("password", "")
				.param("statusId", "1"))
			.andExpect(status().isOk())
			.andExpect(view().name("patriot/patriotProfile"))
			.andExpect(model().attributeHasFieldErrors("patriotUser", "email"));
	}

	/**
	 * Verifies that a new password that fails the strength requirements (no uppercase)
	 * returns the profile form with a field-level error on {@code password}.
	 */
	@Test
	@DisplayName("POST /patriot/profile -> weak password returns form with error")
	void testProcessProfileUpdateWeakPassword() throws Exception {
		mockMvc
			.perform(post("/patriot/profile").principal(() -> TEST_EMAIL)
				.param("firstName", "John")
				.param("lastName", "Doe")
				.param("email", TEST_EMAIL)
				.param("password", "weakpass")
				.param("statusId", "1"))
			.andExpect(status().isOk())
			.andExpect(view().name("patriot/patriotProfile"))
			.andExpect(model().attributeHasFieldErrors("patriotUser", "password"));
	}

	// ========================================================================
	// ACCOUNT DELETION (Soft Delete)
	// ========================================================================

	/**
	 * Verifies that POST /patriot/delete sets the {@code deletedAt} timestamp on the
	 * user, saves the entity, and redirects to the Patriot Thanks home page with a
	 * farewell flash message.
	 */
	@Test
	@DisplayName("POST /patriot/delete -> soft deletes account and redirects to /patriot")
	void testDeleteAccount() throws Exception {
		mockMvc.perform(post("/patriot/delete").principal(() -> TEST_EMAIL))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/patriot"))
			.andExpect(flash().attributeExists("messageSuccess"));

		verify(patriotUserRepository).save(any(PatriotUser.class));
	}

}
