package org.springframework.samples.petclinic.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.samples.petclinic.user.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.samples.petclinic.school.SchoolRepository;
import org.springframework.samples.petclinic.school.School;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Controller responsible for managing the authenticated user's profile page, including
 * viewing and updating personal information, changing passwords, and performing soft
 * account deletion.
 *
 * <p>
 * This controller is mapped to the {@code /users} path and provides endpoints for
 * displaying the profile form, processing profile updates (with duplicate email
 * detection, phone number normalization, and optional password changes), and
 * soft-deleting the user's account.
 * </p>
 *
 * @author Edward
 */
@Controller
@RequestMapping("/users")
public class ProfileController {

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	private final UserDetailsService userDetailsService;

	private final SchoolRepository schoolRepository;

	/**
	 * Constructs a new {@code ProfileController} with the required dependencies.
	 * @param userRepository the repository for user persistence operations
	 * @param passwordEncoder the encoder for hashing new passwords
	 * @param userDetailsService the service for reloading user details after email
	 * changes
	 * @param schoolRepository the repository for looking up schools by domain
	 */
	public ProfileController(UserRepository userRepository, PasswordEncoder passwordEncoder,
			UserDetailsService userDetailsService, SchoolRepository schoolRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.userDetailsService = userDetailsService;
		this.schoolRepository = schoolRepository;
	}

	/**
	 * This @ModelAttribute makes the language map available to ALL views in this
	 * controller. It pairs with the new selectField fragment we just built.
	 * @return a {@link Map} of language code keys to display name values
	 */
	@ModelAttribute("languageOptions")
	public Map<String, String> populateLanguageOptions() {
		Map<String, String> options = new LinkedHashMap<>();
		options.put("EN", "English");
		options.put("ES", "Spanish");
		options.put("KO", "Korean");
		options.put("PL", "Pig Latin");
		return options;
	}

	/**
	 * Displays the profile editing form for the currently authenticated user.
	 *
	 * <p>
	 * The user's password hash is cleared before sending the entity to the view to
	 * prevent it from being exposed in the HTML form. If the user's phone number is
	 * stored as a raw 10-digit string, it is formatted to {@code (XXX) XXX-XXXX} for
	 * display.
	 * </p>
	 * @param model the {@link Model} to populate with the user object
	 * @param principal the {@link Principal} representing the currently logged-in user
	 * @return the view name for the profile form template
	 */
	@GetMapping("/profile")
	public String showProfileForm(Model model, Principal principal) {
		if (principal == null) {
			return "redirect:/login";
		}
		String email = principal.getName();
		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

		// Clear the password hash so it doesn't get sent to the HTML form
		user.setPassword("");

		// Intercept the 10-digit database string and inject parentheses
		// and hyphen before handing it to Thymeleaf
		String phone = user.getPhone();
		if (phone != null && phone.length() == 10) {
			// Converts 3199999999 into (319) 999-9999
			String formattedPhone = phone.replaceFirst("(\\d{3})(\\d{3})(\\d{4})", "($1) $2-$3");
			user.setPhone(formattedPhone);
		}
		// Extract the school slug by recursively stripping subdomains until a
		// matching school is found (e.g., student@student.kirkwood.edu -> "kirkwood")
		String domain = email.substring(email.indexOf("@") + 1);
		String slug = null;
		String tempDomain = domain;
		while (tempDomain.contains(".")) {
			Optional<School> matchedSchool = schoolRepository.findByDomain(tempDomain);
			if (matchedSchool.isPresent()) {
				// Strip the TLD to get the slug (e.g., "kirkwood.edu" -> "kirkwood")
				String schoolDomain = matchedSchool.get().getDomain();
				slug = schoolDomain.substring(0, schoolDomain.lastIndexOf("."));
				break;
			}
			// Strip the leftmost subdomain and try again
			tempDomain = tempDomain.substring(tempDomain.indexOf(".") + 1);
		}
		model.addAttribute("schoolSlug", slug); // null if no school matched

		model.addAttribute("user", user);
		return "users/profile";
	}

	/**
	 * Processes the profile update form submission for the currently authenticated user.
	 *
	 * <p>
	 * This method handles several alternative flows:
	 * </p>
	 * <ul>
	 * <li>Checks for duplicate emails only if the user is changing their email
	 * address</li>
	 * <li>Validates password strength manually only if a new password was entered</li>
	 * <li>Normalizes the phone number by stripping all non-digit characters before
	 * saving</li>
	 * <li>Refreshes the Spring Security context if the email was changed, preventing a
	 * "User not found" error on the subsequent redirect</li>
	 * </ul>
	 * @param updatedUser the {@link User} object populated from the form, validated with
	 * {@link Valid} (default group only, so password rules are bypassed unless the user
	 * types a new password)
	 * @param result the {@link BindingResult} containing any validation errors
	 * @param principal the {@link Principal} representing the currently logged-in user
	 * @param redirectAttributes the {@link RedirectAttributes} for passing success/error
	 * flash messages across the redirect
	 * @return a redirect to the profile page on success, or the profile form view if
	 * validation errors exist
	 */
	@PostMapping("/profile")
	public String processProfileUpdate(@Valid @ModelAttribute("user") User updatedUser, BindingResult result,
			Principal principal, RedirectAttributes redirectAttributes) {

		String currentEmail = principal.getName();
		User currentUser = userRepository.findByEmail(currentEmail)
			.orElseThrow(() -> new RuntimeException("User not found"));

		// 1. Check for duplicate email ONLY if they are changing their email address
		if (!currentEmail.equalsIgnoreCase(updatedUser.getEmail())) {
			if (userRepository.existsByEmail(updatedUser.getEmail())) {
				result.rejectValue("email", "duplicateEmail", "This email is already taken.");
			}
		}

		// 2. Validate password strength manually
		String newPassword = updatedUser.getPassword();
		boolean isUpdatingPassword = newPassword != null && !newPassword.trim().isEmpty();

		if (isUpdatingPassword) {
			if (!newPassword.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$")) {
				// Add this regex check to enforce the character rules
				result.rejectValue("password", "weakPassword",
						"Password must be at least 8 characters and must contain uppercase, lowercase, and number");
			}
		}

		// 3. Return to form if there are validation errors
		if (result.hasErrors()) {
			return "users/profile";
		}

		// 4. Safely apply the updates to the entity fetched from the DB
		currentUser.setFirstName(updatedUser.getFirstName());
		currentUser.setLastName(updatedUser.getLastName());
		currentUser.setNickname(updatedUser.getNickname());
		currentUser.setEmail(updatedUser.getEmail());
		String submittedPhone = updatedUser.getPhone();
		if (submittedPhone != null && !submittedPhone.trim().isEmpty()) {
			currentUser.setPhone(submittedPhone.replaceAll("\\D", "")); // Strips all
																		// non-numbers
		}
		else {
			currentUser.setPhone(null);
		}
		currentUser.setPublicEmail(updatedUser.getPublicEmail());
		currentUser.setPublicPhone(updatedUser.getPublicPhone());
		currentUser.setPreferredLanguage(updatedUser.getPreferredLanguage());

		if (isUpdatingPassword) {
			currentUser.setPassword(passwordEncoder.encode(newPassword));
		}

		// 5. Save the updates to the database
		userRepository.save(currentUser);

		// 6. Update the Spring Security Context if the email changed
		if (!currentEmail.equalsIgnoreCase(currentUser.getEmail())) {

			// Fetch the freshly updated user details
			UserDetails newPrincipal = userDetailsService.loadUserByUsername(currentUser.getEmail());
			Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();

			// Create a new authentication token with the new email
			Authentication newAuth = new UsernamePasswordAuthenticationToken(newPrincipal, currentAuth.getCredentials(),
					newPrincipal.getAuthorities());

			// Replace the old token in the session memory
			SecurityContextHolder.getContext().setAuthentication(newAuth);
		}

		// 7. Redirect
		redirectAttributes.addFlashAttribute("messageSuccess", "Your profile has been updated successfully.");

		// Grab the 2-letter code from the database object and make it lowercase
		String langCode = currentUser.getPreferredLanguage();
		if (langCode != null) {
			// This triggers the LocaleChangeInterceptor we just built!
			return "redirect:/users/profile?lang=" + langCode.toLowerCase();
		}

		return "redirect:/users/profile";
	}

	/**
	 * Performs a soft delete on the currently authenticated user's account.
	 *
	 * <p>
	 * Instead of permanently removing the user record from the database, this method
	 * stamps the {@code deletedAt} field with the current date and time. This preserves
	 * historical data integrity (e.g., past league scores or team rosters) while
	 * effectively deactivating the account. After updating the record, the user's session
	 * is destroyed via {@link SecurityContextLogoutHandler} to prevent further access.
	 * </p>
	 * @param principal the {@link Principal} representing the currently logged-in user
	 * @param request the {@link HttpServletRequest} used for session invalidation
	 * @param response the {@link HttpServletResponse} used for session invalidation
	 * @param redirectAttributes the {@link RedirectAttributes} for passing a farewell
	 * flash message across the redirect
	 * @return a redirect to the home page with a success message
	 */
	@PostMapping("/delete")
	public String deleteAccount(Principal principal, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {

		// 1. Find the user
		String email = principal.getName();
		User currentUser = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

		// 2. Perform the Soft Delete
		currentUser.setDeletedAt(LocalDateTime.now());

		// Optionally, you can scramble personal info if you want to anonymize the record:
		// currentUser.setEmail("deleted_" + currentUser.getId() + "@example.com");
		// currentUser.setFirstName("Deleted");
		// currentUser.setLastName("User");

		userRepository.save(currentUser);

		// 3. Log the user out programmatically
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}

		// 4. Redirect to the homepage with a farewell message
		redirectAttributes.addFlashAttribute("messageSuccess",
				"Your account has been successfully deleted. We're sorry to see you go!");
		return "redirect:/";
	}

}
