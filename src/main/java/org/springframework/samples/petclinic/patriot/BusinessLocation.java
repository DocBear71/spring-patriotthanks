package org.springframework.samples.petclinic.patriot;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import org.springframework.samples.petclinic.model.BaseEntity;

/**
 * Simple JavaBean domain object representing a Business Location. A business can have
 * multiple locations (e.g., chain restaurants, retail stores).
 *
 * @author Edward McKeown
 */
@Entity
@Table(name = "business_locations")
@Getter
@Setter
@SQLDelete(sql = "UPDATE business_locations SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class BusinessLocation extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "business_id")
	private Business business;

	@ManyToOne
	@JoinColumn(name = "address_id")
	private Address address;

	@Column(name = "location_name")
	private String locationName;

	@Column(name = "phone")
	private String phone;

	@Column(name = "email")
	private String email;

	@Column(name = "hours_of_operation")
	private String hoursOfOperation;

	@Column(name = "is_primary")
	private Boolean isPrimary = false;

	@Column(name = "is_active")
	private Boolean isActive = true;

	@Column(name = "created_at", insertable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", insertable = false, updatable = false)
	private LocalDateTime updatedAt;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	/**
	 * Enum representing the active status of a business location.
	 */
	public enum LocationStatus {

		ACTIVE, INACTIVE

	}

}
