package org.springframework.samples.petclinic.user;

import jakarta.annotation.Priority;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom implementation of Spring Security's {@link UserDetailsService} that loads
 * user-specific data from the database during the authentication process.
 *
 * <p>
 * This service is marked as {@link Primary} so that it takes precedence over any other
 * {@link UserDetailsService} beans in the application context. It translates the
 * application's {@link User} entity into a Spring Security
 * {@link org.springframework.security.core.userdetails.User} object.
 * </p>
 *
 * <p>
 * Soft-deleted accounts (where {@code deletedAt} is not {@code null}) are treated as
 * non-existent to prevent former users from logging back in.
 * </p>
 *
 * @author Edward
 */
@Primary
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	/**
	 * Constructs a new {@code UserDetailsServiceImpl} with the required
	 * {@link UserRepository} dependency.
	 * @param userRepository the repository used to look up users by email
	 */
	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * Loads a {@link UserDetails} object by the user's email address for Spring Security
	 * authentication.
	 *
	 * <p>
	 * If no user is found with the given email, or if the user's account has been
	 * soft-deleted (i.e., {@code deletedAt} is not {@code null}), a
	 * {@link UsernameNotFoundException} is thrown. The generic error message prevents
	 * malicious actors from determining which emails are registered or which accounts
	 * have been deleted.
	 * </p>
	 * @param email the email address identifying the user whose data is required
	 * @return a fully populated {@link UserDetails} object for authentication
	 * @throws UsernameNotFoundException if the user is not found or has been soft-deleted
	 */
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// 1. Find the user via the UserRepository
		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException("Invalid email or password."));

		// 2. Block soft-deleted accounts from logging in
		if (user.getDeletedAt() != null) {
			throw new UsernameNotFoundException("Invalid email or password.");
		}

		// 3. Convert your custom User model into the UserDetails object that Spring
		// Security understands
		return org.springframework.security.core.userdetails.User.builder()
			.username(user.getEmail())
			.password(user.getPassword())
			.roles(user.getRoles().stream().map(role -> role.getName()).toArray(String[]::new))
			.build();
	}

}
