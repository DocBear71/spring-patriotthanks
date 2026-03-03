package org.springframework.samples.petclinic.patriot;

import jakarta.persistence.*;
import lombok.*;

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
 * @author Edward McKeown
 * @see PatriotUser
 */
@Entity
@Table(name = "patriot_roles")
@Data
@NoArgsConstructor
public class PatriotRole {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, unique = true, length = 50)
	private String name;

	@Column(length = 255)
	private String description;

	@ManyToMany(mappedBy = "roles")
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Set<PatriotUser> users;

}
