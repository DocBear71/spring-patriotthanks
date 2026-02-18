package org.springframework.samples.petclinic.patriot;

import java.util.Collection;

import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository interface for {@link BusinessType} entities. Provides methods to retrieve
 * business type data for use in forms and lookups.
 *
 * @author Edward McKeown
 */
public interface BusinessTypeRepository extends Repository<BusinessType, Integer> {

	/**
	 * Retrieve all {@link BusinessType} records from the data store, ordered by display
	 * order.
	 * @return a {@link Collection} of {@link BusinessType BusinessTypes}
	 */
	@Transactional(readOnly = true)
	Collection<BusinessType> findAllByOrderByDisplayOrderAsc();

	/**
	 * Retrieve a {@link BusinessType} by its ID.
	 * @param id the ID of the BusinessType to retrieve
	 * @return the {@link BusinessType} with the given ID, or null if not found
	 */
	@Transactional(readOnly = true)
	BusinessType findById(Integer id);

}
