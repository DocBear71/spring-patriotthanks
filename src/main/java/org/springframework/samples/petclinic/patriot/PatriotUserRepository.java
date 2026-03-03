package org.springframework.samples.petclinic.patriot;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for {@link PatriotUser} entities. Provides CRUD operations
 * and custom query methods for the {@code patriot_users} table.
 *
 * @author Edward McKeown
 */
public interface PatriotUserRepository extends JpaRepository<PatriotUser, Integer> {

	/**
	 * Finds a Patriot Thanks user by their email address.
	 * @param email the email address to search for
	 * @return an {@link Optional} containing the user if found, or empty if not
	 */
	Optional<PatriotUser> findByEmail(String email);

	/**
	 * Checks whether a Patriot Thanks user with the given email already exists.
	 * @param email the email address to check
	 * @return {@code true} if a user with the given email exists, {@code false} otherwise
	 */
	boolean existsByEmail(String email);

}
