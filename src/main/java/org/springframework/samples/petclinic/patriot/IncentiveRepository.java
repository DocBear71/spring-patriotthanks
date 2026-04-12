package org.springframework.samples.petclinic.patriot;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository interface for Incentive entities. Provides methods to retrieve, save, and
 * delete incentive data.
 *
 * @author Edward McKeown
 */
public interface IncentiveRepository extends Repository<Incentive, Integer> {

	/**
	 * Retrieve all incentives for a specific business.
	 * @param businessId the ID of the business
	 * @return a List of Incentives for the given business
	 */
	@Transactional(readOnly = true)
	List<Incentive> findByBusinessId(Integer businessId);

	/**
	 * Retrieve all active incentives for a specific business with incentive types eagerly
	 * loaded.
	 * @param businessId the ID of the business
	 * @param isActive whether the incentive is active
	 * @return a List of active Incentives for the given business
	 */
	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT i FROM Incentive i LEFT JOIN FETCH i.incentiveTypes WHERE i.business.id = :businessId AND i.isActive = :isActive")
	List<Incentive> findByBusinessIdAndIsActive(@Param("businessId") Integer businessId,
			@Param("isActive") Boolean isActive);

	/**
	 * Retrieve an Incentive by its ID with its incentive types eagerly loaded.
	 * @param id the ID of the Incentive to retrieve
	 * @return the Incentive with the given ID, or null if not found
	 */
	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT i FROM Incentive i LEFT JOIN FETCH i.incentiveTypes WHERE i.id = :id")
	Incentive findByIdWithTypes(@Param("id") Integer id);

	/**
	 * Retrieve an Incentive by its ID.
	 * @param id the ID of the Incentive to retrieve
	 * @return the Incentive with the given ID, or null if not found
	 */
	@Transactional(readOnly = true)
	Incentive findById(Integer id);

	/**
	 * Save an Incentive to the data store. Used for both create and update operations.
	 * @param incentive the Incentive to save
	 */
	void save(Incentive incentive);

	/**
	 * Delete (soft-delete) an Incentive from the data store. The {@code @SQLDelete}
	 * annotation on {@link Incentive} intercepts this call and sets
	 * {@code deleted_at = NOW()} instead of issuing a physical DELETE.
	 * @param incentive the Incentive to soft-delete
	 */
	void delete(Incentive incentive);

}
