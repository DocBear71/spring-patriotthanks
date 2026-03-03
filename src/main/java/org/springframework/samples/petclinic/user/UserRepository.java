package org.springframework.samples.petclinic.user;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Spring Data JPA repository for {@link User} entities.
 *
 * <p>
 * Provides CRUD operations via {@link JpaRepository} along with custom query
 * methods for looking up users by email and checking for email uniqueness.
 * </p>
 *
 * @author Edward
 */
public interface UserRepository extends JpaRepository<User, Integer> {

	/**
	 * Retrieves a user by their email address.
	 *
	 * @param email the email address to search for
	 * @return an {@link Optional} containing the matching {@link User},
	 *         or {@link Optional#empty()} if no user is found
	 */
	Optional<User> findByEmail(String email);

	/**
	 * Checks whether a user with the given email address already exists in the database.
	 *
	 * <p>
	 * Used during profile updates to detect duplicate emails before saving, without
	 * needing to load the full {@link User} entity.
	 * </p>
	 *
	 * @param email the email address to check for existence
	 * @return {@code true} if a user with this email exists, {@code false} otherwise
	 */
	boolean existsByEmail(String email);

}
