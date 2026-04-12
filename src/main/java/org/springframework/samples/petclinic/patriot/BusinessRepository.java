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
 * business data with pagination support, including lookup by ID, slug, and eager-fetched
 * detail queries.
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

	/**
	 * Retrieve a Business by its URL-friendly slug with all associated locations and
	 * incentives eagerly fetched. Uses {@code LEFT JOIN FETCH} to load the lazy
	 * collections in a single query, avoiding {@code LazyInitializationException} when
	 * rendering the business details view.
	 * @param slug the URL-friendly slug of the Business (e.g., "olive-garden")
	 * @return an {@link Optional} containing the fully-loaded Business if found, or empty
	 * if not
	 */
	@Query("SELECT DISTINCT b FROM Business b " + "LEFT JOIN FETCH b.locations " + "LEFT JOIN FETCH b.incentives "
			+ "WHERE b.slug = :slug")
	@Transactional(readOnly = true)
	Optional<Business> findBySlugWithDetails(@Param("slug") String slug);

	/**
	 * Retrieve a paginated list of businesses filtered by name keyword only
	 * (case-insensitive partial match). Used when a keyword is provided but no type
	 * filter. Avoids the {@code ? IS NULL} pattern unsupported by MySQL 5.7.
	 * @param keyword the partial business name to search for
	 * @param pageable pagination information
	 * @return a {@link Page} of matching {@link Business} records
	 */
	@Query("SELECT b FROM Business b WHERE LOWER(b.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	@Transactional(readOnly = true)
	Page<Business> findByNameContaining(@Param("keyword") String keyword, Pageable pageable);

	/**
	 * Retrieve a paginated list of businesses filtered by business type only. Used when a
	 * type ID is provided but no keyword. Avoids the {@code ? IS NULL} pattern
	 * unsupported by MySQL 5.7.
	 * @param typeId the ID of the {@link BusinessType} to filter by
	 * @param pageable pagination information
	 * @return a {@link Page} of matching {@link Business} records
	 */
	@Transactional(readOnly = true)
	Page<Business> findByBusinessTypeId(Integer typeId, Pageable pageable);

	/**
	 * Retrieve a paginated list of businesses filtered by both name keyword and business
	 * type. Used when both filters are provided. Avoids the {@code ? IS NULL} pattern
	 * unsupported by MySQL 5.7.
	 * @param keyword the partial business name to search for
	 * @param typeId the ID of the {@link BusinessType} to filter by
	 * @param pageable pagination information
	 * @return a {@link Page} of matching {@link Business} records
	 */
	@Query("SELECT b FROM Business b WHERE LOWER(b.name) LIKE LOWER(CONCAT('%', :keyword, '%')) AND b.businessType.id = :typeId")
	@Transactional(readOnly = true)
	Page<Business> findByNameContainingAndTypeId(@Param("keyword") String keyword, @Param("typeId") Integer typeId,
			Pageable pageable);

	/**
	 * Delete (soft-delete) a Business from the data store. The {@code @SQLDelete}
	 * annotation on {@link Business} intercepts this call and sets
	 * {@code deleted_at = NOW()} instead of issuing a physical DELETE.
	 * @param business the Business to soft-delete
	 */
	void delete(Business business);

}
