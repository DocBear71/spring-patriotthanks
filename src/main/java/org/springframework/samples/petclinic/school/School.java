package org.springframework.samples.petclinic.school;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import org.springframework.samples.petclinic.model.NamedEntity;
import org.springframework.samples.petclinic.validation.UniqueDomain;

/**
 * Simple JavaBean domain object representing a School.
 *
 * @author Edward McKeown
 */
@Entity
@Table(name = "schools")
@UniqueDomain // <--- validate unique domain
@Getter
@Setter
// Intercept the delete command and turn it into an update
@SQLDelete(sql = "UPDATE schools SET deleted_at = NOW() WHERE id = ?")
// Automatically filter out deleted rows when reading data
@SQLRestriction("deleted_at IS NULL")
public class School extends NamedEntity {

	@Column(name = "domain", unique = true)
	@NotEmpty
	private String domain;

	@Enumerated(EnumType.STRING)
	@Column(name = "status_id")
	private SchoolStatus status = SchoolStatus.ACTIVE;

	@Column(name = "created_at", insertable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", insertable = false, updatable = false)
	private LocalDateTime updatedAt;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "school", fetch = FetchType.EAGER)
	private List<Location> locations = new ArrayList<>();

	/**
	 * Adds a location to this school. Establishes the bidirectional relationship between
	 * school and location.
	 * @param location the location to add to this school
	 */
	public void addLocation(Location location) {
		location.setSchool(this);
		getLocations().add(location);
	}

	/**
	 * Enum representing the status of a school.
	 */
	public enum SchoolStatus {

		ACTIVE, INACTIVE, SUSPENDED

	}

}
