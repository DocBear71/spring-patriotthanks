package org.springframework.samples.petclinic.patriot;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.samples.petclinic.model.BaseEntity;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * JPA entity representing a user in the Patriot Thanks system. This entity is completely
 * separate from the AthLeagues {@code User} entity and maps to the {@code patriot_users}
 * table.
 *
 * <p>
 * Patriot Thanks users include veterans, active duty service members, first responders,
 * military spouses, business owners, and supporters. Each user is assigned a
 * {@code statusId} corresponding to one of these categories and may hold one or more
 * {@link PatriotRole} entries.
 * </p>
 *
 * @author Edward McKeown
 * @see PatriotRole
 * @see PatriotUserRepository
 */
@Entity
@Table(name = "patriot_users")
@Data
@NoArgsConstructor
public class PatriotUser extends BaseEntity {

	@Column(name = "first_name", length = 256)
	@NotEmpty(message = "First name is required")
	private String firstName;

	@Column(name = "last_name", length = 256)
	@NotEmpty(message = "Last name is required")
	private String lastName;

	@Column(nullable = false, unique = true, length = 255)
	@NotEmpty(message = "Email is required")
	@Email(message = "Please enter a valid email")
	private String email;

	@Column(name = "password_hash", nullable = false, length = 255)
	@NotEmpty(message = "Password is required")
	@Size(min = 8, message = "Password must be at least 8 characters")
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$",
		message = "Password must contain uppercase, lowercase, and a number")
	private String password;

	@Column(name = "phone", length = 20)
	@Pattern(regexp = "^\\+?[0-9\\-\\s]*$", message = "Please enter a valid phone number")
	private String phone;

	@Column(name = "status_id", nullable = false)
	private Integer statusId;

	@Column(name = "email_verified")
	private Boolean emailVerified = false;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
		name = "patriot_user_roles",
		joinColumns = @JoinColumn(name = "patriot_user_id"),
		inverseJoinColumns = @JoinColumn(name = "patriot_role_id")
	)
	@EqualsAndHashCode.Exclude
	private Set<PatriotRole> roles = new LinkedHashSet<>();

}
