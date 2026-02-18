package org.springframework.samples.petclinic.school;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import org.springframework.samples.petclinic.model.NamedEntity;

/**
 * Simple JavaBean domain object representing a Location within a School.
 *
 * @author Edward McKeown
 */
@Entity
@Table(name = "locations")
@Getter
@Setter
@SQLDelete(sql = "UPDATE locations SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Location extends NamedEntity {

	@ManyToOne
	@JoinColumn(name = "school_id")
	private School school;

	@ManyToOne
	@JoinColumn(name = "parent_location_id")
	private Location parentLocation;

	@Column(name = "description")
	private String description;

	@Column(name = "address")
	private String address;

	@Column(name = "latitude")
	private BigDecimal latitude;

	@Column(name = "longitude")
	private BigDecimal longitude;

	@Enumerated(EnumType.STRING)
	@Column(name = "status_id")
	private LocationStatus status = LocationStatus.ACTIVE;

	@Column(name = "created_at", insertable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", insertable = false, updatable = false)
	private LocalDateTime updatedAt;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	/**
	 * Enum representing the status of a location.
	 */
	public enum LocationStatus {

		DRAFT, ACTIVE, CLOSED, COMING_SOON

	}

}
