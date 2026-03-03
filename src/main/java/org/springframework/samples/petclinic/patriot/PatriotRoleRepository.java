package org.springframework.samples.petclinic.patriot;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for {@link PatriotRole} entities. Provides CRUD operations
 * and custom query methods for the {@code patriot_roles} table.
 *
 * @author Edward McKeown
 */
public interface PatriotRoleRepository extends JpaRepository<PatriotRole, Integer> {

	/**
	 * Finds a Patriot Thanks role by its unique name.
	 * @param name the role name to search for (e.g., {@code "VETERAN"},
	 *             {@code "BUSINESS_OWNER"})
	 * @return an {@link Optional} containing the role if found, or empty if not
	 */
	Optional<PatriotRole> findByName(String name);

}
