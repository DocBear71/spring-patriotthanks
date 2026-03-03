package org.springframework.samples.petclinic.patriot;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Spring Security {@link UserDetailsService} implementation for the Patriot Thanks
 * authentication system. Loads user details from the {@code patriot_users} table and
 * converts them into a Spring Security {@link UserDetails} object for authentication.
 *
 * <p>
 * This service is separate from the AthLeagues {@code UserDetailsServiceImpl} and is
 * used exclusively by the Patriot Thanks security filter chain. It also enforces soft
 * delete logic by rejecting users whose {@code deletedAt} timestamp is non-null.
 * </p>
 *
 * @author Edward McKeown
 * @see PatriotSecurityConfig
 */
@Service("patriotUserDetailsService")
public class PatriotUserDetailsServiceImpl implements UserDetailsService {

	private final PatriotUserRepository patriotUserRepository;

	/**
	 * Constructs a new {@code PatriotUserDetailsServiceImpl} with the required
	 * repository.
	 * @param patriotUserRepository the repository for looking up Patriot Thanks users
	 */
	public PatriotUserDetailsServiceImpl(PatriotUserRepository patriotUserRepository) {
		this.patriotUserRepository = patriotUserRepository;
	}

	/**
	 * Loads a {@link PatriotUser} by email and converts it to a Spring Security
	 * {@link UserDetails} object. Soft-deleted accounts (where {@code deletedAt} is
	 * non-null) are rejected with the same error message as invalid credentials to
	 * prevent account enumeration.
	 * @param email the user's email address (used as the username)
	 * @return a {@link UserDetails} object containing the user's credentials and roles
	 * @throws UsernameNotFoundException if no user is found or the account has been
	 *                                   soft-deleted
	 */
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// 1. Find the user
		PatriotUser user = patriotUserRepository.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException("Invalid email or password."));

		// 2. Block soft-deleted accounts
		if (user.getDeletedAt() != null) {
			throw new UsernameNotFoundException("Invalid email or password.");
		}

		// 3. Convert to Spring Security UserDetails
		return org.springframework.security.core.userdetails.User.builder()
			.username(user.getEmail())
			.password(user.getPassword())
			.roles(user.getRoles().stream()
				.map(PatriotRole::getName)
				.toArray(String[]::new))
			.build();
	}

}
