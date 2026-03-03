package org.springframework.samples.petclinic.patriot;

/**
 * Service interface for Patriot Thanks user management. Handles business logic for user
 * registration, including password hashing and default role assignment.
 *
 * @author Edward McKeown
 * @see PatriotUserServiceImpl
 */
public interface PatriotUserService {

	/**
	 * Registers a new Patriot Thanks user. The implementation is responsible for hashing
	 * the user's password and assigning the appropriate default role based on their
	 * selected status.
	 * @param user the {@link PatriotUser} object containing the new user's details
	 * @return the saved {@link PatriotUser} with generated ID and encoded password
	 * @throws RuntimeException if the default role cannot be found in the database
	 */
	PatriotUser registerNewUser(PatriotUser user);

}
