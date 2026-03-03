package org.springframework.samples.petclinic.patriot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for the {@link PatriotUserDetailsServiceImpl}. Verifies that the service
 * correctly loads user details for Spring Security authentication and properly rejects
 * accounts that have been soft-deleted.
 *
 * @author Edward McKeown
 * @see PatriotUserDetailsServiceImpl
 */
@ExtendWith(MockitoExtension.class)
class PatriotUserDetailsServiceImplTest {

	@Mock
	private PatriotUserRepository patriotUserRepository;

	@InjectMocks
	private PatriotUserDetailsServiceImpl userDetailsService;

	private PatriotUser activeUser;

	private PatriotUser deletedUser;

	private PatriotRole veteranRole;

	/**
	 * Sets up test data before each test method. Creates an active user and a
	 * soft-deleted user with the VETERAN role.
	 */
	@BeforeEach
	void setUp() {
		veteranRole = new PatriotRole();
		veteranRole.setId(1);
		veteranRole.setName("VETERAN");

		LinkedHashSet<PatriotRole> roles = new LinkedHashSet<>();
		roles.add(veteranRole);

		activeUser = new PatriotUser();
		activeUser.setId(1);
		activeUser.setFirstName("John");
		activeUser.setLastName("Doe");
		activeUser.setEmail("john.doe@example.com");
		activeUser.setPassword("$2a$10$hashedpassword");
		activeUser.setStatusId(1);
		activeUser.setRoles(roles);
		activeUser.setDeletedAt(null);

		deletedUser = new PatriotUser();
		deletedUser.setId(2);
		deletedUser.setFirstName("Jane");
		deletedUser.setLastName("Deleted");
		deletedUser.setEmail("jane.deleted@example.com");
		deletedUser.setPassword("$2a$10$hashedpassword");
		deletedUser.setStatusId(1);
		deletedUser.setRoles(roles);
		deletedUser.setDeletedAt(LocalDateTime.now());
	}

	/**
	 * Verifies that a valid, active user is loaded successfully with the correct email
	 * (username) and roles.
	 */
	@Test
	@DisplayName("loadUserByUsername -> returns UserDetails for active user")
	void testLoadActiveUser() {
		given(patriotUserRepository.findByEmail("john.doe@example.com")).willReturn(Optional.of(activeUser));

		UserDetails userDetails = userDetailsService.loadUserByUsername("john.doe@example.com");

		assertThat(userDetails).isNotNull();
		assertThat(userDetails.getUsername()).isEqualTo("john.doe@example.com");
		assertThat(userDetails.getPassword()).isEqualTo("$2a$10$hashedpassword");
		assertThat(userDetails.getAuthorities()).hasSize(1);
		assertThat(userDetails.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_VETERAN");
	}

	/**
	 * Verifies that a non-existent email throws a {@link UsernameNotFoundException},
	 * preventing account enumeration.
	 */
	@Test
	@DisplayName("loadUserByUsername -> throws exception for unknown email")
	void testLoadUnknownUserThrowsException() {
		given(patriotUserRepository.findByEmail("unknown@example.com")).willReturn(Optional.empty());

		assertThatThrownBy(() -> userDetailsService.loadUserByUsername("unknown@example.com"))
			.isInstanceOf(UsernameNotFoundException.class)
			.hasMessage("Invalid email or password.");
	}

	/**
	 * Verifies that a soft-deleted user (where {@code deletedAt} is non-null) is rejected
	 * with the same generic error message as an unknown user, preventing attackers from
	 * determining which accounts have been deleted.
	 */
	@Test
	@DisplayName("loadUserByUsername -> throws exception for soft-deleted user")
	void testLoadDeletedUserThrowsException() {
		given(patriotUserRepository.findByEmail("jane.deleted@example.com")).willReturn(Optional.of(deletedUser));

		assertThatThrownBy(() -> userDetailsService.loadUserByUsername("jane.deleted@example.com"))
			.isInstanceOf(UsernameNotFoundException.class)
			.hasMessage("Invalid email or password.");
	}

	/**
	 * Verifies that the service correctly maps multiple roles when a user holds more than
	 * one {@link PatriotRole}.
	 */
	@Test
	@DisplayName("loadUserByUsername -> maps multiple roles correctly")
	void testLoadUserWithMultipleRoles() {
		PatriotRole businessOwnerRole = new PatriotRole();
		businessOwnerRole.setId(5);
		businessOwnerRole.setName("BUSINESS_OWNER");

		LinkedHashSet<PatriotRole> multipleRoles = new LinkedHashSet<>();
		multipleRoles.add(veteranRole);
		multipleRoles.add(businessOwnerRole);
		activeUser.setRoles(multipleRoles);

		given(patriotUserRepository.findByEmail("john.doe@example.com")).willReturn(Optional.of(activeUser));

		UserDetails userDetails = userDetailsService.loadUserByUsername("john.doe@example.com");

		assertThat(userDetails.getAuthorities()).hasSize(2);
	}

}
