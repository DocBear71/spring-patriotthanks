package org.springframework.samples.petclinic.patriot;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

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
	 * @return an {@link Optional} containing the Business if found, or empty if not
	 */
	@Transactional(readOnly = true)
	Optional<Business> findById(Integer id);

	/**
	 * Retrieve a Business by its ID with all associated locations and incentives eagerly
	 * fetched. Uses {@code LEFT JOIN FETCH} to load the lazy collections in a single
	 * query, avoiding {@code LazyInitializationException} when rendering the business
	 * details view.
	 * @param id the ID of the Business to retrieve
	 * @return an {@link Optional} containing the fully-loaded Business if found, or empty
	 * if not
	 */
	@Query("SELECT DISTINCT b FROM Business b " + "LEFT JOIN FETCH b.locations " + "LEFT JOIN FETCH b.incentives "
			+ "WHERE b.id = :id")
	@Transactional(readOnly = true)
	Optional<Business> findByIdWithDetails(@Param("id") Integer id);

}
