package org.springframework.samples.petclinic.patriot;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for {@link PatriotPermission} entities. Provides standard
 * CRUD operations and a lookup method by permission name.
 *
 * @author Edward McKeown
 * @see PatriotPermission
 */
public interface PatriotPermissionRepository extends JpaRepository<PatriotPermission, Integer> {

	/**
	 * Finds a {@link PatriotPermission} by its unique name.
	 * @param name the permission name to search for (e.g. {@code "SUBMIT_BUSINESS"})
	 * @return an {@link Optional} containing the permission if found, or empty
	 */
	Optional<PatriotPermission> findByName(String name);

}
