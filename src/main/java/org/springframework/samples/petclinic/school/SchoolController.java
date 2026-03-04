package org.springframework.samples.petclinic.school;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.samples.petclinic.user.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Map;

/**
 * Controller responsible for managing school-related views including the school list,
 * school creation form, and individual school detail pages.
 *
 * <p>
 * School detail pages can be accessed by numeric ID (e.g., {@code /schools/1}) or by a
 * slug derived from the school's domain (e.g., {@code /schools/kirkwood}). When accessed
 * by slug, the controller also loads the authenticated user's profile data to display as
 * a "Player Card" sidebar.
 * </p>
 *
 * @author Edward
 */
@Controller
public class SchoolController {

	private final SchoolRepository schoolRepository;

	private final UserRepository userRepository;

	/**
	 * Constructs a new {@code SchoolController} with the required dependencies.
	 * @param schoolRepository the repository for school persistence operations
	 * @param userRepository the repository for looking up the authenticated user's
	 * profile
	 */
	public SchoolController(SchoolRepository schoolRepository, UserRepository userRepository) {
		this.schoolRepository = schoolRepository;
		this.userRepository = userRepository;
	}

	/**
	 * Displays a paginated list of all schools.
	 * @param page the page number to display (1-indexed, defaults to 1)
	 * @param model the {@link Model} to populate with pagination data and school list
	 * @return the view name for the school list template
	 */
	@GetMapping("/schools")
	public String showSchoolList(@RequestParam(defaultValue = "1") int page, Model model) {
		// Pagination setup (5 items per page)
		Pageable pageable = PageRequest.of(page - 1, 5);
		Page<School> schoolPage = schoolRepository.findAll(pageable);

		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", schoolPage.getTotalPages());
		model.addAttribute("totalItems", schoolPage.getTotalElements());
		model.addAttribute("listSchools", schoolPage.getContent());

		return "schools/schoolList";
	}

	/**
	 * Displays the form for creating a new school with a blank {@link School} object.
	 * @param model the model map to populate with a blank {@link School}
	 * @return the view name for the create/update school form template
	 */
	@GetMapping("/schools/new")
	public String initCreationForm(Map<String, Object> model) {
		// Phase 1 of Sequence Diagram
		// 1. Create the blank object (State: New)
		School school = new School();
		// 2. Add it to the model so the Thymeleaf form can bind data to it
		model.put("school", school);
		// 3. Return the view
		return "schools/createOrUpdateSchoolForm";
	}

	/**
	 * Processes the school creation form submission. Validates input and saves the new
	 * school to the database.
	 * @param school the {@link School} object populated from the form
	 * @param result the {@link BindingResult} containing any validation errors
	 * @return a redirect to the school list on success, or the form view if validation
	 * fails
	 */
	@PostMapping("/schools/new")
	public String processCreationForm(@Valid School school, BindingResult result) {
		// Phase 2 of Sequence Diagram
		// 1. Check Validation
		if (result.hasErrors()) {
			// Validation Failed: Return to the form to show errors
			return "schools/createOrUpdateSchoolForm";
		}
		// 2. Save Data (Validation Passed)
		// Note: The status defaults to ACTIVE because of your School.java definition
		schoolRepository.save(school);
		// 3. Redirect to the list
		return "redirect:/schools";
	}

	/**
	 * Displays the school detail page for a school looked up by its numeric ID.
	 * @param schoolId the numeric ID of the school to display
	 * @return a {@link ModelAndView} containing the school object and the detail view
	 * name
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if no school
	 * matches the given ID
	 */
	// Matches ONLY numbers (e.g., /schools/1)
	@GetMapping("/schools/{schoolId:\\d+}")
	public ModelAndView showSchoolById(@PathVariable("schoolId") int schoolId) {
		ModelAndView mav = new ModelAndView("schools/schoolDetails");
		School school = schoolRepository.findById(schoolId)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
					"School with id " + schoolId + " not found."));
		mav.addObject(school);
		return mav;
	}

	/**
	 * Displays the school detail page for a school looked up by its domain slug.
	 *
	 * <p>
	 * The slug is converted to a full domain by appending {@code ".edu"} (e.g.,
	 * {@code "kirkwood"} becomes {@code "kirkwood.edu"}). If the user is authenticated,
	 * their profile data is loaded and added to the model as {@code currentUser} so it
	 * can be displayed in the "Player Card" sidebar. The user's phone number is formatted
	 * from a raw 10-digit string to {@code (XXX) XXX-XXXX} for display.
	 * </p>
	 * @param slug the domain slug portion of the school (e.g., {@code "kirkwood"})
	 * @param principal the {@link Principal} representing the currently logged-in user,
	 * or {@code null} if the visitor is a guest
	 * @return a {@link ModelAndView} containing the school and optional user objects
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if no school
	 * matches the reconstructed domain
	 */
	// Matches text (e.g., /schools/kirkwood)
	@GetMapping("/schools/{slug:[a-zA-Z-]+}")
	public ModelAndView showSchoolBySlug(@PathVariable("slug") String slug, Principal principal) {
		// Reconstruct the domain (User asked to assume ".edu")
		String fullDomain = slug + ".edu";

		ModelAndView mav = new ModelAndView("schools/schoolDetails");
		School school = schoolRepository.findByDomain(fullDomain)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
					"School with domain '" + fullDomain + "' not found."));
		mav.addObject(school);

		// Fetch and add the logged-in user if they are authenticated
		if (principal != null) {
			userRepository.findByEmail(principal.getName()).ifPresent(user -> {

				// Format the 10-digit database phone number for display
				String phone = user.getPhone();
				if (phone != null && phone.length() == 10) {
					user.setPhone(phone.replaceFirst("(\\d{3})(\\d{3})(\\d{4})", "($1) $2-$3"));
				}

				mav.addObject("currentUser", user);
			});
		}

		return mav;
	}

}
