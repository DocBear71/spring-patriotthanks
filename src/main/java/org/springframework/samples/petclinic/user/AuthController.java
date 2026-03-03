package org.springframework.samples.petclinic.user;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.samples.petclinic.user.*;
import org.springframework.samples.petclinic.school.School;
import org.springframework.samples.petclinic.school.SchoolRepository;
import org.springframework.samples.petclinic.validation.OnRegister;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import java.security.Principal;
import java.util.Optional;

/**
 * Controller responsible for handling user authentication operations including
 * registration and login. Uses Spring MVC to serve Thymeleaf templates and handle form
 * submissions.
 *
 * <p>
 * After successful registration, the controller captures the raw password before hashing,
 * saves the user, auto-logs the user in via {@link AuthenticationManager}, then attempts
 * to match the user's email domain (including subdomains) to a {@link School} in the
 * database and redirects accordingly.
 * </p>
 *
 * @author Edward
 */
@Controller
public class AuthController {

	private final UserService userService;
	private final SchoolRepository schoolRepository;
	private final AuthenticationManager authenticationManager;
	private final SecurityContextRepository securityContextRepository =
		new HttpSessionSecurityContextRepository();


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
	@GetMapping("/register-student")
	public String initRegisterForm(Model model) {
		model.addAttribute("user", new User());
		return "auth/registerForm";
	}

	/**
	 * Processes the registration form submission. Validates the user input, saves the new
	 * user via {@link UserService}, auto-logs the user in, and redirects based on the
	 * user's email domain.
	 *
	 * <p>
	 * If the email domain (or a parent domain) matches a {@link School} in the database,
	 * the user is redirected to that school's page. Otherwise, the user is redirected to
	 * the home page.
	 * </p>
	 * @param user the {@link User} object populated from the form, validated with
	 * {@link Valid}
	 * @param result the {@link BindingResult} containing any validation errors
	 * @param redirectAttributes the {@link RedirectAttributes} for passing flash messages
	 * across redirects
	 * @return a redirect URL to the matched school page or the home page, or the
	 * registration form view if there are validation errors
	 */
	@PostMapping("/register-student")
	public String registerUser(@Validated(OnRegister.class) @ModelAttribute("user") User user,
							   BindingResult result,
							   RedirectAttributes redirectAttributes,
							   HttpServletRequest request,
							   HttpServletResponse response) {
		if (result.hasErrors()) {
			return "auth/registerForm";
		}

		String rawPassword = user.getPassword();

		// 1. Save the User (UserService handles password hashing)
		try {
			userService.registerNewStudent(user);
		}
		catch (RuntimeException ex) {
			// Handle duplicate email or other service errors
			result.rejectValue("email", "duplicateEmail", "This email is already registered");
			return "auth/registerForm";
		}
		// to do: send email verification before auto log in.
		// 2. LOGIN using the authenticationManager
		try {
			UsernamePasswordAuthenticationToken authToken =
				new UsernamePasswordAuthenticationToken(user.getEmail(), rawPassword);
			Authentication authentication = authenticationManager.authenticate(authToken);

			// Create a new SecurityContext and persist it to the HTTP session
			SecurityContext context = SecurityContextHolder.createEmptyContext();
			context.setAuthentication(authentication);
			SecurityContextHolder.setContext(context);
			securityContextRepository.saveContext(context, request, response);
		}
		catch (Exception e) {
			redirectAttributes.addFlashAttribute("messageDanger", "Account created, but auto-login failed.");
			return "redirect:/login";
		}
		// 3. Marc's project will redirect a new user
		String email = user.getEmail();
		Optional<School> school = findSchoolByRecursiveDomain(email);

		if (school.isPresent()) {
			redirectAttributes.addFlashAttribute("messageSuccess",
				"Your user account is created. You have been redirected to " + school.get().getName() + "'s school page.");
			return "redirect:/schools/" + school.get().getDomain().substring(0, school.get().getDomain().length() - 4);
		}
		else {
			redirectAttributes.addFlashAttribute("messageWarning",
				"Your user account is created, but we could not find a school matching your email domain.");
			// Fallback if no school matches the email domain
			return "redirect:/";
		}
	}

	/**
	 * Displays the login form.
	 * @return the view name for the login form template
	 */
	@GetMapping("/login")
	public String initLoginForm(Model model, HttpSession session) {
		User user = new User();

		// Grab the failed email attempt from Spring Security's session memory
		String lastEmail = (String) session.getAttribute("LAST_EMAIL");
		if (lastEmail != null) {
			user.setEmail(lastEmail);
			session.removeAttribute("LAST_EMAIL"); // Clean up the session
		}

		model.addAttribute("user", user);
		return "auth/loginForm";
	}

	/**
	 * Recursively searches for a {@link School} matching the email's domain by
	 * progressively stripping subdomains. For example, given the email
	 * {@code "alex@student.kirkwood.edu"}, this method will check:
	 * <ol>
	 * <li>{@code "student.kirkwood.edu"}</li>
	 * <li>{@code "kirkwood.edu"}</li>
	 * </ol>
	 * and return the first matching school found.
	 * @param email the user's email address to extract the domain from
	 * @return an {@link Optional} containing the matched {@link School}, or
	 * {@link Optional#empty()} if no school matches
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

	@GetMapping("/login-success")
	public String processLoginSuccess(Principal principal, RedirectAttributes redirectAttributes) {
		// 1. Get the logged-in user's email
		String email = principal.getName();

		// 2. Reuse your existing method to find their school
		Optional<School> school = findSchoolByRecursiveDomain(email);

		// 3. Redirect them exactly like you did in the registration POST method
		if(school.isPresent()) {
			redirectAttributes.addFlashAttribute("messageSuccess",
				"Welcome back! You have been redirected to " + school.get().getName() + ".");
			return "redirect:/schools/" + school.get().getDomain().substring(0, school.get().getDomain().length() - 4);
		} else {
			redirectAttributes.addFlashAttribute("messageWarning",
				"Welcome back! We could not find a school matching your email domain.");
			return "redirect:/schools";
		}
	}



}
