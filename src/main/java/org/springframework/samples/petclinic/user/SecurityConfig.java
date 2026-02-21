package org.springframework.samples.petclinic.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security configuration for the PetClinic application. Defines password encoding,
 * authentication management, and HTTP security rules including URL-based access control.
 *
 * @author Edward
 */
@Configuration
public class SecurityConfig {

	/**
	 * Creates a {@link BCryptPasswordEncoder} bean for hashing user passwords.
	 * @return a {@link PasswordEncoder} using the BCrypt hashing algorithm
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Exposes the {@link AuthenticationManager} as a Spring bean so it can be injected
	 * into controllers for programmatic authentication (e.g., auto-login after
	 * registration).
	 * @param config the {@link AuthenticationConfiguration} provided by Spring
	 * @return the configured {@link AuthenticationManager}
	 * @throws Exception if the authentication manager cannot be built
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	/**
	 * Configures the HTTP security filter chain with URL-based access rules.
	 *
	 * <p>
	 * Current rules:
	 * </p>
	 * <ul>
	 * <li>All GET requests are permitted (public page viewing)</li>
	 * <li>{@code /register} and {@code /login} are open for POST</li>
	 * <li>{@code /schools/new} and {@code /owners/new} POST are temporarily permitted for
	 * form validation testing</li>
	 * <li>All other requests require authentication</li>
	 * </ul>
	 * @param http the {@link HttpSecurity} to configure
	 * @return the built {@link SecurityFilterChain}
	 * @throws Exception if an error occurs during configuration
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(authorize -> authorize
				// This allows unmapped paths to result in 404, and allows all web
				// viewing.
				.requestMatchers(HttpMethod.GET)
				.permitAll()

				// Allow POST for user registration and login
				.requestMatchers("/register", "/login", "/schools/new", "/owners/new", "/businesses/new")
				.permitAll()

				// TEMPORARY: Allow anonymous users to POST to these forms
				// .requestMatchers(HttpMethod.POST, "/schools/new",
				// "/owners/new").permitAll()

				// PROTECTED CATCH-ALL (This protects unlisted POST/PUT/DELETE, etc.)
				.anyRequest()
				.authenticated())
			// Ensure all auto-challenge mechanisms are disabled
			.httpBasic(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable);

		return http.build();
	}

}
