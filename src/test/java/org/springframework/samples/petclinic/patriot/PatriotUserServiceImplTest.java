package org.springframework.samples.petclinic.patriot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the {@link PatriotUserServiceImpl}. Verifies that user registration
 * correctly hashes passwords, assigns the appropriate default role based on the user's
 * selected status, and persists the user.
 *
 * @author Edward McKeown
 * @see PatriotUserServiceImpl
 */
@ExtendWith(MockitoExtension.class)
class PatriotUserServiceImplTest {

	@Mock
	private PatriotUserRepository patriotUserRepository;

	@Mock
	private PatriotRoleRepository patriotRoleRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private PatriotUserServiceImpl patriotUserService;

	private PatriotUser newUser;

	private PatriotRole veteranRole;

	private PatriotRole activeDutyRole;

	private PatriotRole firstResponderRole;

	private PatriotRole businessOwnerRole;

	private PatriotRole supporterRole;

	/**
	 * Sets up test data before each test method. Creates test roles and a new
	 * {@link PatriotUser} with valid registration data.
	 */
	@BeforeEach
	void setUp() {
		veteranRole = new PatriotRole();
		veteranRole.setId(1);
		veteranRole.setName("VETERAN");

		activeDutyRole = new PatriotRole();
		activeDutyRole.setId(2);
		activeDutyRole.setName("ACTIVE_DUTY");

		firstResponderRole = new PatriotRole();
		firstResponderRole.setId(3);
		firstResponderRole.setName("FIRST_RESPONDER");

		businessOwnerRole = new PatriotRole();
		businessOwnerRole.setId(5);
		businessOwnerRole.setName("BUSINESS_OWNER");

		supporterRole = new PatriotRole();
		supporterRole.setId(6);
		supporterRole.setName("SUPPORTER");

		newUser = new PatriotUser();
		newUser.setFirstName("John");
		newUser.setLastName("Doe");
		newUser.setEmail("john.doe@example.com");
		newUser.setPassword("Password1");
		newUser.setStatusId(1);
	}

	/**
	 * Verifies that registering a veteran (statusId = 1) hashes the password,
	 * assigns the VETERAN role, and saves the user.
	 */
	@Test
	@DisplayName("registerNewUser -> veteran status assigns VETERAN role")
	void testRegisterVeteranUser() {
		given(passwordEncoder.encode("Password1")).willReturn("$2a$10$encodedPassword");
		given(patriotRoleRepository.findByName("VETERAN")).willReturn(Optional.of(veteranRole));
		given(patriotUserRepository.save(any(PatriotUser.class))).willReturn(newUser);

		PatriotUser result = patriotUserService.registerNewUser(newUser);

		assertThat(result).isNotNull();
		verify(passwordEncoder).encode("Password1");
		verify(patriotRoleRepository).findByName("VETERAN");
		verify(patriotUserRepository).save(newUser);
		assertThat(newUser.getRoles()).contains(veteranRole);
	}

	/**
	 * Verifies that registering an active duty user (statusId = 2) assigns
	 * the ACTIVE_DUTY role.
	 */
	@Test
	@DisplayName("registerNewUser -> active duty status assigns ACTIVE_DUTY role")
	void testRegisterActiveDutyUser() {
		newUser.setStatusId(2);
		given(passwordEncoder.encode(anyString())).willReturn("$2a$10$encodedPassword");
		given(patriotRoleRepository.findByName("ACTIVE_DUTY")).willReturn(Optional.of(activeDutyRole));
		given(patriotUserRepository.save(any(PatriotUser.class))).willReturn(newUser);

		patriotUserService.registerNewUser(newUser);

		verify(patriotRoleRepository).findByName("ACTIVE_DUTY");
		assertThat(newUser.getRoles()).contains(activeDutyRole);
	}

	/**
	 * Verifies that registering a first responder (statusId = 3) assigns
	 * the FIRST_RESPONDER role.
	 */
	@Test
	@DisplayName("registerNewUser -> first responder status assigns FIRST_RESPONDER role")
	void testRegisterFirstResponderUser() {
		newUser.setStatusId(3);
		given(passwordEncoder.encode(anyString())).willReturn("$2a$10$encodedPassword");
		given(patriotRoleRepository.findByName("FIRST_RESPONDER")).willReturn(Optional.of(firstResponderRole));
		given(patriotUserRepository.save(any(PatriotUser.class))).willReturn(newUser);

		patriotUserService.registerNewUser(newUser);

		verify(patriotRoleRepository).findByName("FIRST_RESPONDER");
		assertThat(newUser.getRoles()).contains(firstResponderRole);
	}

	/**
	 * Verifies that registering a business owner (statusId = 5) assigns
	 * the BUSINESS_OWNER role.
	 */
	@Test
	@DisplayName("registerNewUser -> business owner status assigns BUSINESS_OWNER role")
	void testRegisterBusinessOwnerUser() {
		newUser.setStatusId(5);
		given(passwordEncoder.encode(anyString())).willReturn("$2a$10$encodedPassword");
		given(patriotRoleRepository.findByName("BUSINESS_OWNER")).willReturn(Optional.of(businessOwnerRole));
		given(patriotUserRepository.save(any(PatriotUser.class))).willReturn(newUser);

		patriotUserService.registerNewUser(newUser);

		verify(patriotRoleRepository).findByName("BUSINESS_OWNER");
		assertThat(newUser.getRoles()).contains(businessOwnerRole);
	}

	/**
	 * Verifies that the raw password is replaced with a hashed version
	 * after registration.
	 */
	@Test
	@DisplayName("registerNewUser -> password is hashed before saving")
	void testPasswordIsHashed() {
		given(passwordEncoder.encode("Password1")).willReturn("$2a$10$encodedPassword");
		given(patriotRoleRepository.findByName("VETERAN")).willReturn(Optional.of(veteranRole));
		given(patriotUserRepository.save(any(PatriotUser.class))).willReturn(newUser);

		patriotUserService.registerNewUser(newUser);

		assertThat(newUser.getPassword()).isEqualTo("$2a$10$encodedPassword");
	}

	/**
	 * Verifies that if the role repository cannot find the expected role, a
	 * {@link RuntimeException} is thrown with an informative message.
	 */
	@Test
	@DisplayName("registerNewUser -> throws exception when role not found")
	void testRegisterUserRoleNotFound() {
		given(passwordEncoder.encode(anyString())).willReturn("$2a$10$encodedPassword");
		given(patriotRoleRepository.findByName("VETERAN")).willReturn(Optional.empty());

		assertThatThrownBy(() -> patriotUserService.registerNewUser(newUser))
			.isInstanceOf(RuntimeException.class)
			.hasMessageContaining("Role not found: VETERAN");
	}

	/**
	 * Verifies that an unrecognized statusId defaults to the SUPPORTER role.
	 */
	@Test
	@DisplayName("registerNewUser -> unknown statusId defaults to SUPPORTER role")
	void testRegisterUnknownStatusDefaultsToSupporter() {
		newUser.setStatusId(999);
		given(passwordEncoder.encode(anyString())).willReturn("$2a$10$encodedPassword");
		given(patriotRoleRepository.findByName("SUPPORTER")).willReturn(Optional.of(supporterRole));
		given(patriotUserRepository.save(any(PatriotUser.class))).willReturn(newUser);

		patriotUserService.registerNewUser(newUser);

		verify(patriotRoleRepository).findByName("SUPPORTER");
		assertThat(newUser.getRoles()).contains(supporterRole);
	}

}
