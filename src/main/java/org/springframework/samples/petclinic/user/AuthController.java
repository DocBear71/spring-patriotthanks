package org.springframework.samples.petclinic.user;

import jakarta.validation.Valid;
import org.springframework.samples.petclinic.school.School;
import org.springframework.samples.petclinic.school.SchoolRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * Controller responsible for handling user authentication operations
 * including registration and login. Uses Spring MVC to serve Thymeleaf
 * templates and handle form submissions.
 *
 * <p>After successful registration, the controller captures the raw password
 * before hashing, saves the user, auto-logs the user in via
 * {@link AuthenticationManager}, then attempts to match the user's email
 * domain (including subdomains) to a {@link School} in the database and
 * redirects accordingly.</p>
 *
 * @author Edward
 */
@Controller
public class AuthController {

	private final UserService userService;
	private final SchoolRepository schoolRepository;
	private final AuthenticationManager authenticationManager;

	/**
	 * Constructs a new {@code AuthController} with the required dependencies.
	 * @param userService the service for user registration and management
	 * @param schoolRepository the repository for school domain lookups
	 * @param authenticationManager the manager for authenticating users after
	 * registration
	 */
	public AuthController(UserService userService, SchoolRepository schoolRepository,
						  AuthenticationManager authenticationManager) {
		this.userService = userService;
		this.schoolRepository = schoolRepository;
		this.authenticationManager = authenticationManager;
	}

	/**
	 * Displays the user registration form with a blank {@link User} object.
	 * @param model the {@link Model} to populate with a blank {@link User}
	 * @return the view name for the registration form template
	 */
	@GetMapping("/register")
	public String initRegisterForm(Model model) {
		model.addAttribute("user", new User());
		return "auth/registerForm";
	}

//	@PostMapping("/register")
//	public String registerUser(@Valid User user, BindingResult result
//								RedirectAttributes redirectAttributes){
//
//		if (result.hasErrors()) {
//			return "auth/registerForm";
//		}
//
//		String rawPassword = user.getPassword();
//
//		// 1. Save the User (UserService handles password hashing)
//		try {
//			userService.registerNewUser(user);
//		}
//		catch (RuntimeException ex) {
//			// Handle duplicate email or other service errors
//			result.rejectValue("email", "duplicate", "This email is already registered");
//			return "auth/registerForm";
//		}
//
//		// 2. LOGIN using the authenticationManager
//		try {
//			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user.getEmail(),
//				rawPassword);
//			Authentication authentication = authenticationManager.authenticate(authToken);
//			SecurityContextHolder.getContext().setAuthentication(authentication);
//		}
//		catch (Exception e) {
//			redirectAttributes.addFlashAttribute("messageDanger", "Account created, but auto-login failed.");
//			return "redirect:/login";
//		}
//
//		Optional<School> school = findSchoolByRecursiveDomain(user.getEmail());
//
//		if (school.isPresent()) {
//			return "redirect:/schools/" + school.get().getId();
//		}
//		else {
//			// Fallback if no school matches the email domain
//			return "redirect:/";
//		}
//		return "auth/registerForm";
//	}

	/**
	 * Processes the registration form submission. Validates the user input,
	 * saves the new user via {@link UserService}, auto-logs the user in,
	 * and redirects based on the user's email domain.
	 *
	 * <p>If the email domain (or a parent domain) matches a {@link School}
	 * in the database, the user is redirected to that school's page.
	 * Otherwise, the user is redirected to the home page.</p>
	 *
	 * @param user the {@link User} object populated from the form, validated
	 *             with {@link Valid}
	 * @param result the {@link BindingResult} containing any validation errors
	 * @param redirectAttributes the {@link RedirectAttributes} for passing flash
	 *                           messages across redirects
	 * @return a redirect URL to the matched school page or the home page,
	 *         or the registration form view if there are validation errors
	 */
	@PostMapping("/register")
	public String registerUser(@Valid User user, BindingResult result,
									  RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "auth/registerForm";
		}

		String rawPassword = user.getPassword();

		// 1. Save the User (UserService handles password hashing)
		try {
			userService.registerNewUser(user);
		}
		catch (RuntimeException ex) {
			// Handle duplicate email or other service errors
			result.rejectValue("email", "duplicate", "This email is already registered");
			return "auth/registerForm";
		}

		// 2. LOGIN using the authenticationManager
		try {
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user.getEmail(),
				rawPassword);
			Authentication authentication = authenticationManager.authenticate(authToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		catch (Exception e) {
			redirectAttributes.addFlashAttribute("messageDanger", "Account created, but auto-login failed.");
			return "redirect:/login";
		}
		// Marc's project will redirect a new user to their school
		String email = user.getEmail();
		String domain = email.substring(email.indexOf("@") + 1);
		Optional<School> school = findSchoolByRecursiveDomain(user.getEmail());

		if (school.isPresent()) {
			return "redirect:/schools/" + school.get().getDomain().substring(0, school.get().getDomain().length() - 4);
		} else {
			// Fallback if no school matches the email domain
			return "redirect:/";
		}
	}

	/**
	 * Displays the login form.
	 * @return the view name for the login form template
	 */
	@GetMapping("/login")
	public String initLoginForm() {
		return "auth/loginForm";
	}

	/**
	 * Recursively searches for a {@link School} matching the email's domain
	 * by progressively stripping subdomains. For example, given the email
	 * {@code "alex@student.kirkwood.edu"}, this method will check:
	 * <ol>
	 *     <li>{@code "student.kirkwood.edu"}</li>
	 *     <li>{@code "kirkwood.edu"}</li>
	 * </ol>
	 * and return the first matching school found.
	 * @param email the user's email address to extract the domain from
	 * @return an {@link Optional} containing the matched {@link School},
	 *         or {@link Optional#empty()} if no school matches
	 */
	private Optional<School> findSchoolByRecursiveDomain(String email) {
		// 1. Extract the initial domain (e.g., "student.kirkwood.edu")
		String domain = email.substring(email.indexOf("@") + 1);

		// 2. Loop while the domain is valid (has at least one dot)
		while (domain.contains(".")) {
			// 3. Check Database
			Optional<School> school = schoolRepository.findByDomain(domain);
			if (school.isPresent()) {
				return school; // Found match (e.g., "kirkwood.edu")
			}

			// 4. Strip the first part (e.g., "student.kirkwood.edu" -> "kirkwood.edu")
			int dotIndex = domain.indexOf(".");
			domain = domain.substring(dotIndex + 1);
		}

		return Optional.empty();
	}

}
