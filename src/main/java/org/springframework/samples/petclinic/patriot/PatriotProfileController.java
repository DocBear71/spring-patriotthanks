package org.springframework.samples.petclinic.patriot;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.time.LocalDateTime;

/**
 * Controller responsible for managing the authenticated Patriot Thanks user's profile
 * page, including viewing and updating personal information, changing passwords, zip
 * code, avatar (Gravatar), and performing soft account deletion.
 *
 * <p>
 * This controller is mapped to the {@code /patriot} path and provides endpoints for
 * displaying the profile form, processing profile updates (with duplicate email
 * detection, phone number normalization, optional password changes, Gravatar URL
 * regeneration, and zip code storage), and soft-deleting the user's account.
 * </p>
 *
 * <p>
 * <strong>Gravatar integration:</strong> When the user saves their profile, this
 * controller computes an MD5 hash of the (trimmed, lower-cased) email address and
 * constructs a Gravatar URL of the form
 * {@code https://www.gravatar.com/avatar/<hash>?d=identicon&s=200}. This URL is stored in
 * the {@code avatar_url} column and displayed in the profile header. If the email
 * changes, the Gravatar URL is automatically regenerated.
 * </p>
 *
 * <p>
 * <strong>Zip code:</strong> The user's home zip code is stored in the {@code zip_code}
 * column and used as the default geographic search area on the Find Businesses page.
 * Browser-based real-time geolocation is handled entirely on the client side and is never
 * persisted to the database.
 * </p>
 *
 * <p>
 * Note: {@code @Valid} is intentionally NOT used on the POST method because the
 * {@link PatriotUser} entity has {@code @NotEmpty} and {@code @Pattern} constraints on
 * the password field, which would incorrectly reject blank passwords on the profile form.
 * Password validation is performed manually only when a new password is provided.
 * </p>
 *
 * @author Edward McKeown
 * @see PatriotUser
 * @see PatriotUserRepository
 */
@Controller
@RequestMapping("/patriot")
public class PatriotProfileController {

	private final PatriotUserRepository patriotUserRepository;

	private final PasswordEncoder passwordEncoder;

	private final UserDetailsService patriotUserDetailsService;

	/**
	 * Constructs a new {@code PatriotProfileController} with the required dependencies.
	 * @param patriotUserRepository the repository for Patriot Thanks user persistence
	 * @param passwordEncoder the encoder for hashing new passwords
	 * @param patriotUserDetailsService the Patriot Thanks user details service for
	 * reloading user details after email changes
	 */
	public PatriotProfileController(PatriotUserRepository patriotUserRepository, PasswordEncoder passwordEncoder,
			@Qualifier("patriotUserDetailsService") UserDetailsService patriotUserDetailsService) {
		this.patriotUserRepository = patriotUserRepository;
		this.passwordEncoder = passwordEncoder;
		this.patriotUserDetailsService = patriotUserDetailsService;
	}

	// ========================================================================
	// GRAVATAR UTILITY
	// ========================================================================

	/**
	 * Computes a Gravatar avatar URL for the given email address.
	 *
	 * <p>
	 * The URL is constructed by MD5-hashing the trimmed, lower-cased email and appending
	 * it to the Gravatar base URL. The {@code d=identicon} parameter causes Gravatar to
	 * generate a unique geometric placeholder image for users who have not registered a
	 * Gravatar account, ensuring the avatar is never blank.
	 * </p>
	 * @param email the user's email address
	 * @return a fully-formed Gravatar URL string, or {@code null} if hashing fails
	 */
	private String buildGravatarUrl(String email) {
		try {
			String normalized = email.trim().toLowerCase();
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] hashBytes = md.digest(normalized.getBytes(StandardCharsets.UTF_8));
			StringBuilder hex = new StringBuilder();
			for (byte b : hashBytes) {
				hex.append(String.format("%02x", b));
			}
			return "https://www.gravatar.com/avatar/" + hex + "?d=identicon&s=200";
		}
		catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	// ========================================================================
	// PROFILE - GET
	// ========================================================================

	/**
	 * Displays the profile editing form for the currently authenticated Patriot Thanks
	 * user.
	 *
	 * <p>
	 * The user's password hash is cleared before sending the entity to the view to
	 * prevent it from being exposed in the HTML form. If the user's phone number is
	 * stored as a raw 10-digit string, it is formatted to {@code (XXX) XXX-XXXX} for
	 * display. If the user does not yet have an {@code avatarUrl}, a Gravatar URL is
	 * generated from the current email and persisted before rendering.
	 * </p>
	 * @param model the {@link Model} to populate with the user object
	 * @param principal the {@link Principal} representing the currently logged-in user
	 * @return the view name for the Patriot Thanks profile form template
	 */
	@GetMapping("/profile")
	public String showProfileForm(Model model, Principal principal) {
		String email = principal.getName();
		PatriotUser user = patriotUserRepository.findByEmail(email)
			.orElseThrow(() -> new RuntimeException("User not found"));

		// Seed Gravatar URL if it has never been set
		if (user.getAvatarUrl() == null || user.getAvatarUrl().isBlank()) {
			user.setAvatarUrl(buildGravatarUrl(user.getEmail()));
			patriotUserRepository.save(user);
		}

		// Clear the password hash so it doesn't get sent to the HTML form
		user.setPassword("");

		// Format the 10-digit database phone number for display
		String phone = user.getPhone();
		if (phone != null && phone.length() == 10) {
			String formattedPhone = phone.replaceFirst("(\\d{3})(\\d{3})(\\d{4})", "($1) $2-$3");
			user.setPhone(formattedPhone);
		}

		model.addAttribute("patriotUser", user);
		return "patriot/patriotProfile";
	}

	// ========================================================================
	// PROFILE - POST
	// ========================================================================

	/**
	 * Processes the profile update form submission for the currently authenticated
	 * Patriot Thanks user.
	 *
	 * <p>
	 * {@code @Valid} is intentionally omitted because the {@link PatriotUser} entity
	 * requires a non-empty password matching a complex pattern, but the profile form
	 * allows blank passwords (meaning "don't change"). Validation is performed manually
	 * for each field instead.
	 * </p>
	 *
	 * <p>
	 * This method handles several alternative flows:
	 * </p>
	 * <ul>
	 * <li>Validates that first name and last name are not blank</li>
	 * <li>Validates that email is not blank and is a valid format</li>
	 * <li>Checks for duplicate emails only if the user is changing their email</li>
	 * <li>Validates password strength manually only if a new password was entered</li>
	 * <li>Validates zip code format if one is provided (5-digit or ZIP+4)</li>
	 * <li>Normalizes the phone number by stripping all non-digit characters</li>
	 * <li>Regenerates the Gravatar URL if the email address changed</li>
	 * <li>Refreshes the Spring Security context if the email was changed</li>
	 * </ul>
	 * @param updatedUser the {@link PatriotUser} populated from the form
	 * @param result the {@link BindingResult} containing validation errors
	 * @param principal the {@link Principal} representing the logged-in user
	 * @param redirectAttributes the {@link RedirectAttributes} for flash messages
	 * @return a redirect to the profile page on success, or the form view on errors
	 */
	@PostMapping("/profile")
	public String processProfileUpdate(@ModelAttribute("patriotUser") PatriotUser updatedUser, BindingResult result,
			Principal principal, RedirectAttributes redirectAttributes) {

		String currentEmail = principal.getName();
		PatriotUser currentUser = patriotUserRepository.findByEmail(currentEmail)
			.orElseThrow(() -> new RuntimeException("User not found"));

		// 1. Manually validate required fields (bypassing @Valid to avoid password
		// issues)
		if (updatedUser.getFirstName() == null || updatedUser.getFirstName().trim().isEmpty()) {
			result.rejectValue("firstName", "NotEmpty", "First name is required");
		}
		if (updatedUser.getLastName() == null || updatedUser.getLastName().trim().isEmpty()) {
			result.rejectValue("lastName", "NotEmpty", "Last name is required");
		}
		if (updatedUser.getEmail() == null || updatedUser.getEmail().trim().isEmpty()) {
			result.rejectValue("email", "NotEmpty", "Email is required");
		}
		else if (!updatedUser.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
			result.rejectValue("email", "InvalidEmail", "Please enter a valid email");
		}

		// 2. Check for duplicate email ONLY if they are changing their email address
		if (updatedUser.getEmail() != null && !currentEmail.equalsIgnoreCase(updatedUser.getEmail())) {
			if (patriotUserRepository.existsByEmail(updatedUser.getEmail())) {
				result.rejectValue("email", "duplicateEmail", "This email is already taken.");
			}
		}

		// 3. Validate password strength manually (only if they typed a new one)
		String newPassword = updatedUser.getPassword();
		boolean isUpdatingPassword = newPassword != null && !newPassword.trim().isEmpty();

		if (isUpdatingPassword) {
			if (!newPassword.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$")) {
				result.rejectValue("password", "weakPassword",
						"Password must be at least 8 characters with uppercase, lowercase, and a number");
			}
		}

		// 4. Validate zip code format (only if one was provided)
		String submittedZip = updatedUser.getZipCode();
		if (submittedZip != null && !submittedZip.trim().isEmpty()) {
			if (!submittedZip.trim().matches("^\\d{5}(-\\d{4})?$")) {
				result.rejectValue("zipCode", "invalidZip", "Please enter a valid 5-digit zip code");
			}
		}

		// 5. Return to form if there are validation errors
		if (result.hasErrors()) {
			return "patriot/patriotProfile";
		}

		// 6. Safely apply the updates to the entity fetched from the DB
		currentUser.setFirstName(updatedUser.getFirstName().trim());
		currentUser.setLastName(updatedUser.getLastName().trim());

		boolean emailChanged = !currentEmail.equalsIgnoreCase(updatedUser.getEmail().trim());
		currentUser.setEmail(updatedUser.getEmail().trim());

		// 7. Regenerate Gravatar URL if the email address changed
		if (emailChanged) {
			currentUser.setAvatarUrl(buildGravatarUrl(currentUser.getEmail()));
		}

		// 8. Normalize and save phone number
		if (submittedPhone(updatedUser) != null && !submittedPhone(updatedUser).trim().isEmpty()) {
			currentUser.setPhone(submittedPhone(updatedUser).replaceAll("\\D", ""));
		}
		else {
			currentUser.setPhone(null);
		}

		// 9. Save zip code (null it out if blank)
		if (submittedZip != null && !submittedZip.trim().isEmpty()) {
			currentUser.setZipCode(submittedZip.trim());
		}
		else {
			currentUser.setZipCode(null);
		}

		// 10. Encode and update password if a new one was submitted
		if (isUpdatingPassword) {
			currentUser.setPassword(passwordEncoder.encode(newPassword));
		}

		// 11. Persist all changes
		patriotUserRepository.save(currentUser);

		// 12. Refresh the Spring Security context if the email changed
		if (emailChanged) {
			UserDetails newPrincipal = patriotUserDetailsService.loadUserByUsername(currentUser.getEmail());
			Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
			// Guard against null authentication (e.g. in test environments where the
			// Security context is not fully populated via the filter chain)
			Object credentials = (currentAuth != null) ? currentAuth.getCredentials() : null;
			Authentication newAuth = new UsernamePasswordAuthenticationToken(newPrincipal, credentials,
					newPrincipal.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(newAuth);
		}

		// 13. Redirect with success flash message
		redirectAttributes.addFlashAttribute("messageSuccess", "Your profile has been updated successfully.");
		return "redirect:/patriot/profile";
	}

	/**
	 * Helper to retrieve the submitted phone value from the form-bound user object.
	 * @param updatedUser the form-bound {@link PatriotUser}
	 * @return the raw phone string, or {@code null}
	 */
	private String submittedPhone(PatriotUser updatedUser) {
		return updatedUser.getPhone();
	}

	// ========================================================================
	// ACCOUNT DELETION (Soft Delete)
	// ========================================================================

	/**
	 * Performs a soft delete on the currently authenticated Patriot Thanks user's
	 * account. Sets the {@code deletedAt} timestamp, saves the record, destroys the
	 * session, and redirects to the Patriot Thanks home page with a farewell message.
	 * @param principal the {@link Principal} representing the logged-in user
	 * @param request the {@link HttpServletRequest} for session invalidation
	 * @param response the {@link HttpServletResponse} for session invalidation
	 * @param redirectAttributes the {@link RedirectAttributes} for the farewell message
	 * @return a redirect to the Patriot Thanks home page
	 */
	@PostMapping("/delete")
	public String deleteAccount(Principal principal, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {

		String email = principal.getName();
		PatriotUser currentUser = patriotUserRepository.findByEmail(email)
			.orElseThrow(() -> new RuntimeException("User not found"));

		// Perform the soft delete
		currentUser.setDeletedAt(LocalDateTime.now());
		patriotUserRepository.save(currentUser);

		// Log the user out
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}

		// Redirect with farewell message
		redirectAttributes.addFlashAttribute("messageSuccess",
				"Your Patriot Thanks account has been deleted. Thank you for your service!");
		return "redirect:/patriot";
	}

}
