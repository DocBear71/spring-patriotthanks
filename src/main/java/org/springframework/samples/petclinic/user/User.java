package org.springframework.samples.petclinic.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "first_name", nullable = true, length = 50)
	private String firstName;

	@Column(name = "last_name", nullable = true, length = 50)
	private String lastName;

	@Column(nullable = false, unique = true, length = 100)
	@NotEmpty(message = "Email is required") // Stops empty strings
	@Email(message = "Please enter a valid email") // Enforces email format
	private String email;

	@Column(name = "password_hash", nullable = false, length = 255)
	@NotEmpty(message = "Password is required")
	@Size(min = 8, message = "Password must be at least 8 characters")
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$",
			message = "Password must contain uppercase, lowercase, and number")
	private String password;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	@EqualsAndHashCode.Exclude
	private Set<Role> roles;

}
