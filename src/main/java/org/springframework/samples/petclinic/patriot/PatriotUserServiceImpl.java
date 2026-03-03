package org.springframework.samples.petclinic.patriot;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of {@link PatriotUserService} for the Patriot Thanks system. Handles
 * password hashing via {@link PasswordEncoder} and assigns a default {@link PatriotRole}
 * based on the user's selected status ID.
 *
 * <p>
 * Status-to-role mapping:
 * </p>
 * <ul>
 * <li>1 (Veteran) → {@code VETERAN}</li>
 * <li>2 (Active Duty) → {@code ACTIVE_DUTY}</li>
 * <li>3 (First Responder) → {@code FIRST_RESPONDER}</li>
 * <li>4 (Spouse) → {@code MILITARY_SPOUSE}</li>
 * <li>5 (Business Owner) → {@code BUSINESS_OWNER}</li>
 * <li>6 (Supporter) → {@code SUPPORTER}</li>
 * </ul>
 *
 * @author Edward McKeown
 */
@Service
public class PatriotUserServiceImpl implements PatriotUserService {

	private final PatriotUserRepository patriotUserRepository;

	private final PatriotRoleRepository patriotRoleRepository;

	private final PasswordEncoder passwordEncoder;

	/** Maps status IDs to their default Patriot Thanks role names. */
	private static final Map<Integer, String> STATUS_ROLE_MAP = Map.of(1, "VETERAN", 2, "ACTIVE_DUTY", 3,
			"FIRST_RESPONDER", 4, "MILITARY_SPOUSE", 5, "BUSINESS_OWNER", 6, "SUPPORTER");

	/**
	 * Constructs a new {@code PatriotUserServiceImpl} with the required dependencies.
	 * @param patriotUserRepository the repository for persisting Patriot Thanks users
	 * @param patriotRoleRepository the repository for looking up Patriot Thanks roles
	 * @param passwordEncoder the encoder for hashing user passwords
	 */
	public PatriotUserServiceImpl(PatriotUserRepository patriotUserRepository,
			PatriotRoleRepository patriotRoleRepository, PasswordEncoder passwordEncoder) {
		this.patriotUserRepository = patriotUserRepository;
		this.patriotRoleRepository = patriotRoleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * This implementation hashes the raw password, looks up the default role based on the
	 * user's {@code statusId}, assigns it, and persists the user to the
	 * {@code patriot_users} table.
	 * </p>
	 */
	@Override
	public PatriotUser registerNewUser(PatriotUser user) {
		// 1. Hash the password
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		// 2. Determine the default role from the status
		String roleName = STATUS_ROLE_MAP.getOrDefault(user.getStatusId(), "SUPPORTER");
		PatriotRole defaultRole = patriotRoleRepository.findByName(roleName)
			.orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

		Set<PatriotRole> roles = new LinkedHashSet<>();
		roles.add(defaultRole);
		user.setRoles(roles);

		// 3. Save and return
		return patriotUserRepository.save(user);
	}

}
