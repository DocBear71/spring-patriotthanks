package org.springframework.samples.petclinic.patriot;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.samples.petclinic.model.BaseEntity;

import java.util.HashSet;
import java.util.Set;

/**
 * JPA entity representing a fine-grained permission within the Patriot Thanks system.
 * Permissions are assigned to {@link PatriotRole} entries and are granted to users at
 * login time as Spring Security authorities (without any prefix).
 *
 * <p>
 * Example permission names: {@code "VIEW_INCENTIVES"}, {@code "SUBMIT_BUSINESS"},
 * {@code "MANAGE_BUSINESSES"}, {@code "MANAGE_USERS"}.
 * </p>
 *
 * <p>
 * This entity is separate from the AthLeagues
 * {@link org.springframework.samples.petclinic.user.Permission} entity and maps to the
 * {@code patriot_permissions} table.
 * </p>
 *
 * @author Edward McKeown
 * @see PatriotRole
 * @see PatriotUserDetailsServiceImpl
 */
@Entity
@Table(name = "patriot_permissions")
@Data
@NoArgsConstructor
public class PatriotPermission extends BaseEntity {

	/**
	 * The unique permission name used as a Spring Security authority, e.g.
	 * {@code "SUBMIT_BUSINESS"} or {@code "MANAGE_BUSINESSES"}.
	 */
	@Column(nullable = false, unique = true, length = 100)
	private String name;

	/** Optional human-readable description of what this permission grants. */
	@Column(length = 255)
	private String description;

	/**
	 * The set of roles that have been granted this permission. Mapped by the
	 * {@code permissions} field on {@link PatriotRole}.
	 */
	@ManyToMany(mappedBy = "permissions")
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Set<PatriotRole> roles = new HashSet<>();

}
