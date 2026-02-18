package org.springframework.samples.petclinic.patriot;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * Repository interface for Business entities. Provides methods to retrieve and save
 * business data with pagination support.
 *
 * @author Edward McKeown
 */
public interface BusinessRepository extends Repository<Business, Integer> {

	/**
	 * Retrieve all businesses from the data store.
	 * @return a Collection of Businesses
	 */
	@Transactional(readOnly = true)
	Collection<Business> findAll();

	/**
	 * Retrieve a paginated list of businesses.
	 * @param pageable pagination information
	 * @return a Page of Businesses
	 */
	@Transactional(readOnly = true)
	Page<Business> findAll(Pageable pageable);

	/**
	 * Save a Business to the data store.
	 * @param business the Business to save
	 */
	void save(Business business);

	/**
	 * Retrieve a Business by its ID.
	 * @param id the ID of the Business to retrieve
	 * @return the Business with the given ID, or null if not found
	 */
	@Transactional(readOnly = true)
	Business findById(Integer id);

}
