package org.springframework.samples.petclinic.patriot;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Spring Security {@link UserDetailsService} implementation for the Patriot Thanks
 * authentication system. Loads user details from the {@code patriot_users} table and
 * converts them into a Spring Security {@link UserDetails} object for authentication.
 *
 * <p>
 * This service is separate from the AthLeagues {@code UserDetailsServiceImpl} and is used
 * exclusively by the Patriot Thanks security filter chain. It also enforces soft delete
 * logic by rejecting users whose {@code deletedAt} timestamp is non-null.
 * </p>
 *
 * <p>
 * Authorities are built in two layers:
 * </p>
 * <ol>
 * <li>Each {@link PatriotRole} is added with the {@code ROLE_} prefix (e.g.
 * {@code ROLE_VETERAN}), which allows {@code hasRole()} checks in Spring Security.</li>
 * <li>Each {@link PatriotPermission} attached to a role is added without a prefix (e.g.
 * {@code SUBMIT_BUSINESS}), which allows {@code hasAuthority()} checks in Thymeleaf and
 * security configurations.</li>
 * </ol>
 *
 * @author Edward McKeown
 * @see PatriotSecurityConfig
 * @see PatriotRole
 * @see PatriotPermission
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
	 *
	 * <p>
	 * The returned {@link UserDetails} contains both role-based authorities
	 * ({@code ROLE_VETERAN}) and permission-based authorities ({@code SUBMIT_BUSINESS})
	 * so that both {@code hasRole()} and {@code hasAuthority()} checks work correctly.
	 * </p>
	 * @param email the user's email address (used as the username)
	 * @return a {@link UserDetails} object containing the user's credentials and
	 * authorities
	 * @throws UsernameNotFoundException if no user is found or the account has been
	 * soft-deleted
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

		// 3. Build authorities from roles + permissions
		List<GrantedAuthority> authorities = new ArrayList<>();

		for (PatriotRole role : user.getRoles()) {
			// Add the role with ROLE_ prefix for hasRole() checks
			authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));

			// Add each permission without a prefix for hasAuthority() checks
			for (PatriotPermission permission : role.getPermissions()) {
				authorities.add(new SimpleGrantedAuthority(permission.getName()));
			}
		}

		// 4. Build and return UserDetails
		return org.springframework.security.core.userdetails.User.builder()
			.username(user.getEmail())
			.password(user.getPassword())
			.authorities(authorities)
			.build();
	}

}
