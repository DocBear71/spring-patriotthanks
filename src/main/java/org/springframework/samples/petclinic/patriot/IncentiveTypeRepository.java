package org.springframework.samples.petclinic.patriot;

import java.util.List;

import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository interface for {@link IncentiveType} entities. Provides methods to retrieve
 * incentive type data for use in forms and lookups.
 *
 * @author Edward McKeown
 */
public interface IncentiveTypeRepository extends Repository<IncentiveType, Integer> {

	/**
	 * Retrieve all active {@link IncentiveType} records ordered by display order.
	 * @return a {@link List} of active {@link IncentiveType} records
	 */
	@Transactional(readOnly = true)
	List<IncentiveType> findByIsActiveTrueOrderByDisplayOrderAsc();

	/**
	 * Retrieve an {@link IncentiveType} by its ID.
	 * @param id the ID of the IncentiveType to retrieve
	 * @return the {@link IncentiveType} with the given ID, or {@code null} if not found
	 */
	@Transactional(readOnly = true)
	IncentiveType findById(Integer id);

}
