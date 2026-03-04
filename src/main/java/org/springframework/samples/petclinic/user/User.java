package org.springframework.samples.petclinic.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.validation.OnRegister;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User extends BaseEntity {

	@Column(name = "first_name", length = 50)
	private String firstName;

	@Column(name = "last_name", length = 50)
	private String lastName;

	@Column(name = "nickname", length = 50)
	private String nickname;

	@Column(name = "nickname_is_flagged")
	private Boolean nicknameIsFlagged = false;

	@Column(nullable = false, unique = true, length = 255)
	@NotEmpty(message = "Email is required") // Stops empty strings
	@Email(message = "Please enter a valid email") // Enforces email format
	private String email;

	@Column(name = "public_email")
	private Boolean publicEmail = false;

	@Column(name = "phone", length = 255)
	@Pattern(regexp = "^$|^(?:\\+\\d{1,3}\\s?)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$",
			message = "Please enter a valid phone number")
	private String phone;

	@Column(name = "public_phone")
	private Boolean publicPhone = false;

	// NOTE: You will need to add this column to your schema.sql
	@Column(name = "preferred_language", length = 50)
	private String preferredLanguage;

	@Column(name = "password_hash", nullable = false, length = 255)
	@NotEmpty(message = "Password is required", groups = OnRegister.class)
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$",
			message = "Password must be at least 8 characters, contain uppercase, lowercase, and number",
			groups = OnRegister.class)
	private String password;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	@EqualsAndHashCode.Exclude
	private Set<Role> roles;

}
