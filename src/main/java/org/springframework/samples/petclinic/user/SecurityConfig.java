package org.springframework.samples.petclinic.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security configuration for the AthLeagues application. Defines password
 * encoding, authentication management, and HTTP security rules including URL-based access
 * control.
 *
 * <p>
 * This filter chain is marked with {@link Order @Order(2)} so that the Patriot Thanks
 * security configuration ({@code @Order(1)}) is evaluated first for {@code /patriot/**}
 * routes. The {@link AuthenticationManager} bean is marked {@link Primary @Primary} so
 * that it is the default manager injected when no qualifier is specified (e.g., in
 * {@link org.springframework.samples.petclinic.user.AuthController}).
 * </p>
 *
 * <p>
 * The {@link AuthenticationManager} is built explicitly using a
 * {@link DaoAuthenticationProvider} wired to the AthLeagues
 * {@link UserDetailsServiceImpl} rather than relying on
 * {@link org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration}.
 * This prevents an infinite proxy recursion ({@code StackOverflowError}) that occurs when
 * {@code AuthenticationConfiguration.getAuthenticationManager()} encounters multiple
 * {@link org.springframework.security.core.userdetails.UserDetailsService} beans and
 * wraps the resulting manager in AOP proxies that delegate back to each other.
 * </p>
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
	 * Creates a dedicated {@link AuthenticationManager} for the AthLeagues system. This
	 * manager uses the {@link UserDetailsServiceImpl} bean to look up users in the
	 * {@code users} table.
	 *
	 * <p>
	 * Built as an explicit {@link ProviderManager} with a
	 * {@link DaoAuthenticationProvider} to avoid the infinite proxy recursion that occurs
	 * when {@code AuthenticationConfiguration.getAuthenticationManager()} is used in the
	 * presence of multiple
	 * {@link org.springframework.security.core.userdetails.UserDetailsService} beans.
	 * </p>
	 *
	 * <p>
	 * Marked {@link Primary @Primary} so that this bean is selected by default when
	 * multiple {@link AuthenticationManager} beans exist (e.g., the Patriot Thanks
	 * security configuration also defines its own).
	 * </p>
	 * @param userDetailsService the AthLeagues {@link UserDetailsServiceImpl}
	 * @param passwordEncoder the shared password encoder
	 * @return the configured {@link AuthenticationManager}
	 */
	@Bean
	@Primary
	public AuthenticationManager authenticationManager(UserDetailsServiceImpl userDetailsService,
			PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder);
		return new ProviderManager(provider);
	}

	/**
	 * Configures the HTTP security filter chain with URL-based access rules.
	 *
	 * <p>
	 * Marked {@link Order @Order(2)} so that the Patriot Thanks filter chain
	 * ({@code @Order(1)}) is evaluated first for {@code /patriot/**} routes.
	 * </p>
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
	@Order(2)
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(authorize -> authorize
				// This allows unmapped paths to result in 404, and allows all web
				// viewing.
				.requestMatchers(HttpMethod.GET)
				.permitAll()

				// Allow POST for user registration and login
				.requestMatchers("/register-student", "/login", "/schools/new", "/owners/new", "/businesses/new", "/subscription/new")
				.permitAll()
				.anyRequest()
				.authenticated())

			// Ensure all auto-challenge mechanisms are disabled
			.httpBasic(AbstractHttpConfigurer::disable) // Disable the login popup
			// .formLogin(AbstractHttpConfigurer::disable)
			.formLogin(form -> form.loginPage("/login") // Tells Spring where your custom
														// HTML is
				.usernameParameter("email") // Tells your security configuration to look
											// for email instead of username.
				.defaultSuccessUrl("/login-success", true) // Where to go after successful
															// login
				.failureHandler((request, response, exception) -> {
					request.getSession().setAttribute("LAST_EMAIL", request.getParameter("email"));
					response.sendRedirect("/login?error");
				})
				.permitAll())
			.logout(logout -> logout.logoutUrl("/logout")
				.logoutSuccessUrl("/login?logout") // Triggers the green alert box
				.permitAll());

		return http.build();
	}

}
