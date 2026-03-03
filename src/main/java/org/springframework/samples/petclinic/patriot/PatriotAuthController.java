package org.springframework.samples.petclinic.patriot;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Controller responsible for handling Patriot Thanks user authentication operations
 * including registration, login, login redirection, and account deletion.
 *
 * <p>
 * All routes are prefixed with {@code /patriot} and use the Patriot Thanks-specific
 * {@link AuthenticationManager} backed by the {@code patriot_users} table. This
 * controller is completely independent from the AthLeagues {@code AuthController}.
 * </p>
 *
 * <p>
 * After successful registration, the controller captures the raw password before hashing,
 * saves the user via {@link PatriotUserService}, auto-logs the user in, and redirects to
 * the business listings page with a welcome message.
 * </p>
 *
 * <p>
 * Profile management and account deletion are handled by
 * {@link PatriotProfileController}.
 * </p>
 *
 * @author Edward McKeown
 * @see PatriotSecurityConfig
 * @see PatriotUserService
 * @see PatriotProfileController
 */
@Controller
@RequestMapping("/patriot")
public class PatriotAuthController {

	private final PatriotUserService patriotUserService;

	private final PatriotUserRepository patriotUserRepository;

	private final AuthenticationManager patriotAuthenticationManager;

	private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

	@Value("${turnstile.site-key:1x00000000000000000000AA}")
	private String turnstileSiteKey;

	@Value("${turnstile.secret-key:1x0000000000000000000000000000000AA}")
	private String turnstileSecretKey;

	/**
	 * Constructs a new {@code PatriotAuthController} with the required dependencies.
	 * @param patriotUserService the service for Patriot Thanks user registration
	 * @param patriotUserRepository the repository for Patriot Thanks user lookups
	 * @param patriotAuthenticationManager the Patriot Thanks authentication manager
	 */
	public PatriotAuthController(PatriotUserService patriotUserService, PatriotUserRepository patriotUserRepository,
			@Qualifier("patriotAuthenticationManager") AuthenticationManager patriotAuthenticationManager) {
		this.patriotUserService = patriotUserService;
		this.patriotUserRepository = patriotUserRepository;
		this.patriotAuthenticationManager = patriotAuthenticationManager;
	}

	/**
	 * Populates the status options map for the registration form's radio buttons. This
	 * {@code @ModelAttribute} makes the map available to all views served by this
	 * controller.
	 * @return a {@link Map} of status ID strings to display labels
	 */
	@ModelAttribute("statusOptions")
	public Map<String, String> populateStatusOptions() {
		Map<String, String> options = new LinkedHashMap<>();
		options.put("1", "Veteran");
		options.put("2", "Active Duty");
		options.put("3", "First Responder");
		options.put("4", "Military Spouse");
		options.put("5", "Business Owner");
		options.put("6", "Supporter");
		return options;
	}

	// ========================================================================
	// HOME PAGE
	// ========================================================================

	/**
	 * Displays the Patriot Thanks home page, which provides navigation to business
	 * listings, registration, and login. Uses the Patriot Thanks-specific layout template
	 * with navy/gold branding.
	 * @return the view name for the Patriot Thanks home page
	 */
	@GetMapping({ "", "/" })
	public String showHomePage() {
		return "patriot/patriotHome";
	}

	// ========================================================================
	// REGISTRATION
	// ========================================================================

	/**
	 * Displays the Patriot Thanks user registration form with a blank {@link PatriotUser}
	 * object and the Turnstile site key for CAPTCHA rendering.
	 * @param model the {@link Model} to populate with form data
	 * @return the view name for the Patriot Thanks registration template
	 */
	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("patriotUser", new PatriotUser());
		model.addAttribute("turnstileSiteKey", turnstileSiteKey);
		return "patriot/auth/patriotRegisterForm";
	}

	/**
	 * Processes the Patriot Thanks registration form submission. Validates user input and
	 * the Cloudflare Turnstile CAPTCHA response, saves the new user, auto-logs them in,
	 * and redirects to the business listings page.
	 *
	 * <p>
	 * If the Turnstile token is missing or invalid, the form is returned with an error
	 * message. If the email is already registered, a field-level error is added.
	 * </p>
	 * @param patriotUser the {@link PatriotUser} populated from the form
	 * @param result the {@link BindingResult} containing validation errors
	 * @param turnstileToken the Cloudflare Turnstile response token from the form
	 * @param model the {@link Model} for re-rendering the form on errors
	 * @param redirectAttributes the {@link RedirectAttributes} for flash messages
	 * @param request the {@link HttpServletRequest} for session persistence
	 * @param response the {@link HttpServletResponse} for session persistence
	 * @return a redirect to the business listings or the registration form on errors
	 */
	@PostMapping("/register")
	public String processRegistration(@Valid @ModelAttribute("patriotUser") PatriotUser patriotUser,
			BindingResult result, @RequestParam(name = "cf-turnstile-response", required = false) String turnstileToken,
			Model model, RedirectAttributes redirectAttributes, HttpServletRequest request,
			HttpServletResponse response) {

		// 1. Validate Turnstile CAPTCHA
		if (turnstileToken == null || turnstileToken.isEmpty()) {
			model.addAttribute("turnstileError", "Please complete the CAPTCHA verification.");
			model.addAttribute("turnstileSiteKey", turnstileSiteKey);
			return "patriot/auth/patriotRegisterForm";
		}

		if (!verifyTurnstileToken(turnstileToken, request.getRemoteAddr())) {
			model.addAttribute("turnstileError", "CAPTCHA verification failed. Please try again.");
			model.addAttribute("turnstileSiteKey", turnstileSiteKey);
			return "patriot/auth/patriotRegisterForm";
		}

		// 2. Check for validation errors
		if (result.hasErrors()) {
			model.addAttribute("turnstileSiteKey", turnstileSiteKey);
			return "patriot/auth/patriotRegisterForm";
		}

		// 3. Capture the raw password before hashing
		String rawPassword = patriotUser.getPassword();

		// 4. Save the user
		try {
			patriotUserService.registerNewUser(patriotUser);
		}
		catch (RuntimeException ex) {
			result.rejectValue("email", "duplicateEmail", "This email is already registered.");
			model.addAttribute("turnstileSiteKey", turnstileSiteKey);
			return "patriot/auth/patriotRegisterForm";
		}

		// 5. Auto-login after registration
		try {
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
					patriotUser.getEmail(), rawPassword);
			Authentication authentication = patriotAuthenticationManager.authenticate(authToken);

			SecurityContext context = SecurityContextHolder.createEmptyContext();
			context.setAuthentication(authentication);
			SecurityContextHolder.setContext(context);
			securityContextRepository.saveContext(context, request, response);
		}
		catch (Exception e) {
			redirectAttributes.addFlashAttribute("messageDanger",
					"Account created, but auto-login failed. Please log in.");
			return "redirect:/patriot/login";
		}

		// 6. Redirect to business listings with welcome message
		redirectAttributes.addFlashAttribute("messageSuccess", "Welcome to Patriot Thanks, "
				+ patriotUser.getFirstName() + "! Your account has been created successfully.");
		return "redirect:/businesses";
	}

	// ========================================================================
	// LOGIN
	// ========================================================================

	/**
	 * Displays the Patriot Thanks login form. If a previous login attempt failed, the
	 * email address is pre-populated from the session.
	 * @param model the {@link Model} to populate with the login form data
	 * @param session the {@link HttpSession} for retrieving the last failed email
	 * @return the view name for the Patriot Thanks login template
	 */
	@GetMapping("/login")
	public String showLoginForm(Model model, HttpSession session) {
		PatriotUser user = new PatriotUser();

		// Pre-populate the email if a previous attempt failed
		String lastEmail = (String) session.getAttribute("PATRIOT_LAST_EMAIL");
		if (lastEmail != null) {
			user.setEmail(lastEmail);
			session.removeAttribute("PATRIOT_LAST_EMAIL");
		}

		model.addAttribute("patriotUser", user);
		model.addAttribute("turnstileSiteKey", turnstileSiteKey);
		return "patriot/auth/patriotLoginForm";
	}

	/**
	 * Handles the redirect after a successful Patriot Thanks login. Redirects the
	 * authenticated user to the business listings page with a welcome message.
	 * @param principal the {@link Principal} representing the logged-in user
	 * @param redirectAttributes the {@link RedirectAttributes} for flash messages
	 * @return a redirect to the business listings page
	 */
	@GetMapping("/login-success")
	public String processLoginSuccess(Principal principal, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("messageSuccess", "Welcome back to Patriot Thanks!");
		return "redirect:/businesses";
	}

	// ========================================================================
	// TURNSTILE VERIFICATION
	// ========================================================================

	/**
	 * Verifies the Cloudflare Turnstile CAPTCHA token by sending a POST request to the
	 * Turnstile siteverify endpoint.
	 *
	 * <p>
	 * If the secret key is the Cloudflare test key
	 * ({@code "1x0000000000000000000000000000000AA"}), verification always passes to
	 * support local development without a real Cloudflare account.
	 * </p>
	 * @param token the Turnstile response token from the client-side widget
	 * @param remoteIp the IP address of the client for additional verification
	 * @return {@code true} if the token is valid, {@code false} otherwise
	 */
	private boolean verifyTurnstileToken(String token, String remoteIp) {
		// In development with test keys, always pass
		if ("1x0000000000000000000000000000000AA".equals(turnstileSecretKey)) {
			return true;
		}

		try {
			java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
			String body = "secret=" + java.net.URLEncoder.encode(turnstileSecretKey, "UTF-8") + "&response="
					+ java.net.URLEncoder.encode(token, "UTF-8") + "&remoteip="
					+ java.net.URLEncoder.encode(remoteIp, "UTF-8");

			java.net.http.HttpRequest httpRequest = java.net.http.HttpRequest.newBuilder()
				.uri(java.net.URI.create("https://challenges.cloudflare.com/turnstile/v0/siteverify"))
				.header("Content-Type", "application/x-www-form-urlencoded")
				.POST(java.net.http.HttpRequest.BodyPublishers.ofString(body))
				.build();

			java.net.http.HttpResponse<String> httpResponse = client.send(httpRequest,
					java.net.http.HttpResponse.BodyHandlers.ofString());

			// Simple JSON parsing — look for "success": true
			return httpResponse.body().contains("\"success\":true")
					|| httpResponse.body().contains("\"success\": true");
		}
		catch (Exception e) {
			return false;
		}
	}

}
