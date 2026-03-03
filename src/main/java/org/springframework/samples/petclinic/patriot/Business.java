package org.springframework.samples.petclinic.patriot;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import org.springframework.samples.petclinic.model.NamedEntity;

/**
 * Simple JavaBean domain object representing a Business in the Patriot Thanks system.
 * Businesses offer discounts and incentives to veterans, active military, first
 * responders, and their families.
 *
 * <p>
 * Each business has a URL-friendly {@code slug} derived from its name, used for
 * human-readable routing (e.g., {@code /businesses/olive-garden}). The slug is
 * automatically generated from the name via {@link #generateSlug()} before persist
 * and update operations.
 * </p>
 *
 * @author Edward McKeown
 */
@Entity
@Table(name = "businesses")
@Getter
@Setter
// Intercept the delete command and turn it into an update (soft delete)
@SQLDelete(sql = "UPDATE businesses SET deleted_at = NOW() WHERE id = ?")
// Automatically filter out deleted rows when reading data
@SQLRestriction("deleted_at IS NULL")
public class Business extends NamedEntity {

	@Column(name = "description")
	private String description;

	@Column(name = "website")
	private String website;

	@Column(name = "slug")
	private String slug;

	@ManyToOne
	@JoinColumn(name = "business_type_id")
	@NotNull
	private BusinessType businessType;

	@Column(name = "submitted_by_user_id")
	private Integer submittedByUserId;

	@Column(name = "is_verified")
	private Boolean isVerified = false;

	@Column(name = "is_active")
	private Boolean isActive = true;

	@Column(name = "created_at", insertable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", insertable = false, updatable = false)
	private LocalDateTime updatedAt;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "business", fetch = FetchType.EAGER)
	private Set<BusinessLocation> locations = new LinkedHashSet<>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "business", fetch = FetchType.LAZY)
	private Set<Incentive> incentives = new LinkedHashSet<>();

	/**
	 * Adds a location to this business. Establishes the bidirectional relationship
	 * between business and location.
	 * @param location the location to add to this business
	 */
	public void addLocation(BusinessLocation location) {
		location.setBusiness(this);
		getLocations().add(location);
	}

	/**
	 * Adds an incentive to this business. Establishes the bidirectional relationship
	 * between business and incentive.
	 * @param incentive the incentive to add to this business
	 */
	public void addIncentive(Incentive incentive) {
		incentive.setBusiness(this);
		getIncentives().add(incentive);
	}

	/**
	 * Automatically generates a URL-friendly slug from the business name before the
	 * entity is persisted or updated. The slug is created by converting the name to
	 * lowercase, replacing {@code &} and {@code +} with "and", removing non-alphanumeric
	 * characters (except hyphens and spaces), converting spaces to hyphens, and collapsing
	 * consecutive hyphens.
	 *
	 * <p>
	 * Examples:
	 * </p>
	 * <ul>
	 * <li>"Olive Garden" → "olive-garden"</li>
	 * <li>"Hy-Vee Fast &amp; Fresh" → "hy-vee-fast-and-fresh"</li>
	 * <li>"Culver's" → "culvers"</li>
	 * <li>"Perkins American Food Co." → "perkins-american-food-co"</li>
	 * </ul>
	 */
	@PrePersist
	@PreUpdate
	public void generateSlug() {
		if (getName() != null) {
			this.slug = toSlug(getName());
		}
	}

	/**
	 * Converts a business name to a URL-friendly slug string.
	 * @param name the business name to convert
	 * @return the URL-friendly slug
	 */
	public static String toSlug(String name) {
		return name.toLowerCase()
			.replace("&", "and")
			.replace("+", "and")
			.replaceAll("[^a-z0-9\\s-]", "")
			.trim()
			.replaceAll("\\s+", "-")
			.replaceAll("-+", "-")
			.replaceAll("^-|-$", "");
	}

	/**
	 * Enum representing the active status of a business.
	 */
	public enum BusinessStatus {

		ACTIVE, INACTIVE

	}

}
