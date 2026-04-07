package org.springframework.samples.petclinic.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private RoleRepository roleRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private UserServiceImpl userService;

	@InjectMocks
	private UserDetailsServiceImpl userDetailsService;

	private User testUser;

	private Role studentRole;

	@BeforeEach
	void setUp() {
		testUser = new User();
		testUser.setEmail("example-student@kirkwood.edu");
		// Use "rawPassword" so passwordEncoder.encode("rawPassword") matches the verify
		// call below
		testUser.setPassword("rawPassword");

		// Assign to the class field (not a new local variable) so registerNewUser() can
		// use it
		studentRole = new Role();
		studentRole.setName("STUDENT");

		// Initialize a permission and add it to the role
		Permission viewLeaguesPermission = new Permission();
		viewLeaguesPermission.setName("VIEW_LEAGUES");
		studentRole.setPermissions(Set.of(viewLeaguesPermission));

		testUser.setRoles(Set.of(studentRole));
	}

	@Test
	void loadUserByUsername() {
		// Arrange
		when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));

		// Act
		UserDetails userDetails = userDetailsService.loadUserByUsername(testUser.getEmail());

		// Assert
		assertNotNull(userDetails);
		assertEquals(testUser.getEmail(), userDetails.getUsername());
		assertEquals(testUser.getPassword(), userDetails.getPassword());

		// Check that the ROLE was loaded correctly (Requires "ROLE_" prefix)
		assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT")));

		// Check that the PERMISSION was loaded correctly (No prefix)
		assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("VIEW_LEAGUES")));

		verify(userRepository, times(1)).findByEmail(testUser.getEmail());
	}

	@Test
	void registerNewUser() {
		// --- 1. ARRANGE Mock Behavior (When these methods are called, return this) ---
		// Simulate password hashing: encoder.encode("rawPassword") returns the hashed
		// string
		when(passwordEncoder.encode("rawPassword")).thenReturn("hashedPassword");

		// Simulate role lookup: roleRepository.findByName() should return the STUDENT
		// role
		when(roleRepository.findByName("STUDENT")).thenReturn(Optional.of(studentRole));

		// Simulate save: userRepository.save() should return the user object that was
		// passed to it
		when(userRepository.save(any(User.class))).thenReturn(testUser);

		// --- 2. ACT by calling the method to test ---
		User registeredUser = userService.registerNewStudent(testUser);

		// --- 3. ASSERT by verifying the results ---
		// Check that the user object returned is not null
		assertNotNull(registeredUser);

		// Check that the password was indeed hashed
		assertEquals("hashedPassword", registeredUser.getPassword(), "Password must be hashed.");

		// Check that the STUDENT role was assigned
		assertTrue(registeredUser.getRoles().contains(studentRole), "User must have the STUDENT role.");

		// --- 4. Verify Mock Interactions (Check the service called its dependencies
		// correctly) ---
		// Verify that the encoder was called once
		verify(passwordEncoder, times(1)).encode("rawPassword");

		// Verify that the role repository was called once
		verify(roleRepository, times(1)).findByName("STUDENT");

		// Verify that the user was saved once
		verify(userRepository, times(1)).save(testUser);
	}

}
