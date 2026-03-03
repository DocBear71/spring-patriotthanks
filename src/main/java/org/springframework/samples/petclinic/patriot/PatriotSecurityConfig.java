package org.springframework.samples.petclinic.patriot;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security configuration for the Patriot Thanks application. Defines a separate
 * {@link SecurityFilterChain} for all {@code /patriot/**} and {@code /businesses/**}
 * routes, ordered before the default PetClinic/AthLeagues filter chain so that Patriot
 * Thanks paths are matched first.
 *
 * <p>
 * This configuration uses its own {@link AuthenticationManager} backed by the
 * {@link PatriotUserDetailsServiceImpl} and the shared {@link PasswordEncoder}, ensuring
 * that Patriot Thanks authentication is completely independent from the AthLeagues user
 * system.
 * </p>
 *
 * @author Edward McKeown
 * @see PatriotUserDetailsServiceImpl
 * @see PatriotAuthController
 */
@Configuration
public class PatriotSecurityConfig {

	/**
	 * Creates a dedicated {@link AuthenticationManager} for the Patriot Thanks system.
	 * This manager uses the {@code patriotUserDetailsService} bean to look up users in
	 * the {@code patriot_users} table.
	 *
	 * @param patriotUserDetailsService the Patriot Thanks
	 *                                  {@link UserDetailsService} implementation
	 * @param passwordEncoder           the shared password encoder
	 * @return an {@link AuthenticationManager} for Patriot Thanks authentication
	 */
	@Bean("patriotAuthenticationManager")
	public AuthenticationManager patriotAuthenticationManager(
		@Qualifier("patriotUserDetailsService") UserDetailsService patriotUserDetailsService,
		PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(patriotUserDetailsService);
		provider.setPasswordEncoder(passwordEncoder);
		return new ProviderManager(provider);
	}

	/**
	 * Configures the HTTP security filter chain for all Patriot Thanks routes
	 * ({@code /patriot/**}) and business routes ({@code /businesses/**}). This filter
	 * chain is ordered with {@code @Order(1)} so it takes precedence over the default
	 * PetClinic filter chain.
	 *
	 * <p>
	 * Current rules:
	 * </p>
	 * <ul>
	 *   <li>All GET requests under {@code /patriot/**} and {@code /businesses/**} are
	 *       permitted</li>
	 *   <li>{@code /patriot/register} and {@code /patriot/login} accept POST without
	 *       authentication</li>
	 *   <li>{@code /businesses/new} accepts POST without authentication for form
	 *       validation testing</li>
	 *   <li>All other POST/PUT/DELETE requests under the matched paths require
	 *       authentication</li>
	 *   <li>Custom login page at {@code /patriot/login} using the {@code email}
	 *       parameter</li>
	 *   <li>Successful login redirects to {@code /patriot/login-success}</li>
	 *   <li>Logout posts to {@code /patriot/logout} and redirects to
	 *       {@code /patriot/login?logout}</li>
	 * </ul>
	 *
	 * @param http                        the {@link HttpSecurity} to configure
	 * @param patriotAuthenticationManager the Patriot Thanks authentication manager
	 * @return the built {@link SecurityFilterChain} for Patriot Thanks
	 * @throws Exception if an error occurs during configuration
	 */
	@Bean
	@Order(1)
	public SecurityFilterChain patriotFilterChain(
		HttpSecurity http,
		@Qualifier("patriotAuthenticationManager") AuthenticationManager patriotAuthenticationManager)
		throws Exception {

		http.securityMatcher("/patriot/**", "/businesses/**")
			.authenticationManager(patriotAuthenticationManager)
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/patriot", "/patriot/", "/patriot/register",
					"/patriot/login", "/patriot/login-success")
				.permitAll()
				// Profile requires authentication even for GET
				.requestMatchers("/patriot/profile", "/patriot/delete")
				.authenticated()
				.requestMatchers(org.springframework.http.HttpMethod.GET, "/patriot/**", "/businesses/**")
				.permitAll()
				.requestMatchers("/businesses/new")
				.permitAll()
				.anyRequest()
				.authenticated()
			)
			.httpBasic(AbstractHttpConfigurer::disable)
			.formLogin(form -> form
				.loginPage("/patriot/login")
				.usernameParameter("email")
				.defaultSuccessUrl("/patriot/login-success", true)
				.failureHandler((request, response, exception) -> {
					request.getSession().setAttribute("PATRIOT_LAST_EMAIL",
						request.getParameter("email"));
					response.sendRedirect("/patriot/login?error");
				})
				.permitAll()
			)
			.logout(logout -> logout
				.logoutUrl("/patriot/logout")
				.logoutSuccessUrl("/patriot/login?logout")
				.permitAll()
			);

		return http.build();
	}

}
