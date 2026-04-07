package org.springframework.samples.petclinic.patriot;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * JPA entity representing a role within the Patriot Thanks system. Roles are used to
 * control access to features such as submitting businesses, managing incentives, and
 * viewing analytics.
 *
 * <p>
 * This entity is separate from the AthLeagues {@code Role} entity and maps to the
 * {@code patriot_roles} table. Each role has a unique name (e.g., {@code "VETERAN"},
 * {@code "BUSINESS_OWNER"}, {@code "PLATFORM_ADMIN"}) and an optional description.
 * </p>
 *
 * <p>
 * Each role may have zero or more {@link PatriotPermission} entries assigned via the
 * {@code patriot_role_permissions} join table. At login time,
 * {@link PatriotUserDetailsServiceImpl} loads both the role (as {@code ROLE_<name>}) and
 * all of its permissions as individual Spring Security authorities.
 * </p>
 *
 * @author Edward McKeown
 * @see PatriotUser
 * @see PatriotPermission
 */
@Entity
@Table(name = "patriot_roles")
@Data
@NoArgsConstructor
public class PatriotRole {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/** The unique role name, e.g. {@code "VETERAN"} or {@code "PLATFORM_ADMIN"}. */
	@Column(nullable = false, unique = true, length = 50)
	private String name;

	/** Optional human-readable description of what this role represents. */
	@Column(length = 255)
	private String description;

	/**
	 * The set of users assigned this role. Mapped by the {@code roles} field on
	 * {@link PatriotUser}.
	 */
	@ManyToMany(mappedBy = "roles")
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Set<PatriotUser> users;

	/**
	 * The set of fine-grained permissions granted to users holding this role. Initialized
	 * to an empty {@link HashSet} so iterating over permissions never throws a
	 * {@link NullPointerException} when a role has no permissions assigned.
	 */
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "patriot_role_permissions", joinColumns = @JoinColumn(name = "patriot_role_id"),
			inverseJoinColumns = @JoinColumn(name = "patriot_permission_id"))
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Set<PatriotPermission> permissions = new HashSet<>();

}
