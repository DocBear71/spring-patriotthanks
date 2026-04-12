package org.springframework.samples.petclinic.patriot;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * Controller for handling business-related web requests. Provides endpoints for viewing
 * paginated lists of businesses, their associated incentives, and full CRUD operations
 * for both businesses and their incentives.
 *
 * <p>
 * Businesses can be accessed by numeric ID (which redirects to the slug-based URL) or
 * directly by their URL-friendly slug (e.g., {@code /businesses/olive-garden}). This
 * provides clean, human-readable URLs while maintaining backward compatibility with
 * ID-based links.
 * </p>
 *
 * <p>
 * Incentive CRUD operations are nested under the business slug URL and protected by the
 * {@code MANAGE_BUSINESSES} permission via {@link PatriotSecurityConfig}.
 * </p>
 *
 * @author Edward McKeown
 */
@Controller
public class BusinessController {

	private final BusinessRepository businessRepository;

	private final IncentiveRepository incentiveRepository;

	private final BusinessTypeRepository businessTypeRepository;

	private final IncentiveTypeRepository incentiveTypeRepository;

	/**
	 * Constructor for BusinessController.
	 * @param businessRepository the repository for accessing business data
	 * @param incentiveRepository the repository for accessing incentive data
	 * @param businessTypeRepository the repository for accessing business type data
	 * @param incentiveTypeRepository the repository for accessing incentive type data
	 */
	public BusinessController(BusinessRepository businessRepository, IncentiveRepository incentiveRepository,
			BusinessTypeRepository businessTypeRepository, IncentiveTypeRepository incentiveTypeRepository) {
		this.businessRepository = businessRepository;
		this.incentiveRepository = incentiveRepository;
		this.businessTypeRepository = businessTypeRepository;
		this.incentiveTypeRepository = incentiveTypeRepository;
	}

	/**
	 * Populates the model with all available business types. This method is called before
	 * every request handled by this controller, making the business types available for
	 * form dropdowns.
	 * @return a {@link Collection} of {@link BusinessType} records ordered by display
	 * order
	 */
	@ModelAttribute("types")
	public Collection<BusinessType> populateBusinessTypes() {
		return this.businessTypeRepository.findAllByOrderByDisplayOrderAsc();
	}

	/**
	 * Populates the model with all active incentive types. This method is called before
	 * every request handled by this controller, making the incentive types available for
	 * form checkboxes.
	 * @return a {@link List} of active {@link IncentiveType} records ordered by display
	 * order
	 */
	@ModelAttribute("incentiveTypes")
	public List<IncentiveType> populateIncentiveTypes() {
		return this.incentiveTypeRepository.findByIsActiveTrueOrderByDisplayOrderAsc();
	}

	/**
	 * Prevents Spring MVC from binding the {@code business} field from HTTP request
	 * parameters when populating an {@link Incentive} form object. The business
	 * association is always set programmatically in the controller after validation, so
	 * excluding it here prevents a spurious {@code @NotNull} validation failure on that
	 * field during form submission.
	 * @param binder the {@link WebDataBinder} for {@link Incentive} model attributes
	 */
	@InitBinder("incentive")
	public void initIncentiveBinder(WebDataBinder binder) {
		binder.setDisallowedFields("business");
	}

	// -------------------------------------------------------------------------
	// Business CRUD
	// -------------------------------------------------------------------------

	/**
	 * Displays a paginated, searchable list of businesses. Supports optional filtering by
	 * business name keyword (case-insensitive partial match) and by business type ID.
	 * When no filters are provided the full paginated list is returned.
	 * @param page the page number to display (defaults to 1)
	 * @param keyword optional partial business name to search for
	 * @param typeId optional business type ID to filter by
	 * @param model the Model to add attributes to
	 * @return the view name for the business list page
	 */
	@GetMapping("businesses")
	public String showBusinessList(@RequestParam(defaultValue = "1") int page,
			@RequestParam(required = false) String keyword, @RequestParam(required = false) Integer typeId,
			Model model) {
		Pageable pageable = PageRequest.of(page - 1, 10);

		// Normalise: treat blank keyword as null
		boolean hasKeyword = keyword != null && !keyword.isBlank();
		boolean hasType = typeId != null;
		String effectiveKeyword = hasKeyword ? keyword.trim() : null;

		// Route to the correct targeted query — MySQL 5.7 does not support
		// the "? IS NULL" pattern used in a single combined optional-filter query
		Page<Business> businessPage;
		if (hasKeyword && hasType) {
			businessPage = businessRepository.findByNameContainingAndTypeId(effectiveKeyword, typeId, pageable);
		}
		else if (hasKeyword) {
			businessPage = businessRepository.findByNameContaining(effectiveKeyword, pageable);
		}
		else if (hasType) {
			businessPage = businessRepository.findByBusinessTypeId(typeId, pageable);
		}
		else {
			businessPage = businessRepository.findAll(pageable);
		}

		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", businessPage.getTotalPages());
		model.addAttribute("totalItems", businessPage.getTotalElements());
		model.addAttribute("listBusinesses", businessPage.getContent());
		// Pass search params back to the view so the form retains its values and
		// pagination links include the current filter
		model.addAttribute("keyword", keyword != null ? keyword : "");
		model.addAttribute("selectedTypeId", typeId);
		// Resolve the selected type name for display in the active-filter banner
		// (avoids complex SpEL collection projection in Thymeleaf)
		if (hasType) {
			BusinessType selectedType = businessTypeRepository.findById(typeId);
			model.addAttribute("selectedTypeName", selectedType != null ? selectedType.getName() : "");
		}
		else {
			model.addAttribute("selectedTypeName", "");
		}

		return "businesses/businessList";
	}

	/**
	 * Displays the form for creating a new business.
	 * @param model the Model to add attributes to
	 * @return the view name for the business creation form
	 */
	@GetMapping("/businesses/new")
	public String initCreationForm(Model model) {
		Business business = new Business();
		model.addAttribute("business", business);
		return "businesses/createOrUpdateBusinessForm";
	}

	/**
	 * Processes the form submission for creating a new business. Validates the submitted
	 * data and either saves the business and redirects to the list, or returns the form
	 * with error messages.
	 * @param business the Business object populated from the form data
	 * @param result the BindingResult containing any validation errors
	 * @param model the Model to add attributes to
	 * @return a redirect to the business list on success, or the form view on validation
	 * failure
	 */
	@PostMapping("/businesses/new")
	public String processCreationForm(@Valid Business business, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "businesses/createOrUpdateBusinessForm";
		}
		businessRepository.save(business);
		return "redirect:/businesses";
	}

	/**
	 * Redirects a numeric business ID URL to the slug-based URL for clean, human-readable
	 * routing.
	 *
	 * <p>
	 * Example: {@code /businesses/9} redirects to
	 * {@code /businesses/el-viejo-mexican-restaurant}
	 * </p>
	 * @param businessId the ID of the business to look up
	 * @return a redirect string to the slug-based URL
	 */
	@GetMapping("/businesses/{businessId:\\d+}")
	public String redirectToSlug(@PathVariable("businessId") int businessId) {
		Business business = businessRepository.findById(businessId)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Business with id " + businessId + " not found."));

		String slug = business.getSlug();
		if (slug == null || slug.isBlank()) {
			slug = Business.toSlug(business.getName());
		}
		return "redirect:/businesses/" + slug;
	}

	/**
	 * Displays the details page for a single business, looked up by its URL-friendly
	 * slug. Retrieves the business with all locations and incentives eagerly fetched.
	 *
	 * <p>
	 * Example: {@code /businesses/olive-garden} displays the Olive Garden business
	 * details.
	 * </p>
	 * @param slug the URL-friendly slug derived from the business name
	 * @return a {@link ModelAndView} containing the business details view and the
	 * business object
	 */
	@GetMapping("/businesses/{slug:[a-zA-Z][a-zA-Z0-9-]*}")
	public ModelAndView showBusinessBySlug(@PathVariable("slug") String slug) {
		ModelAndView mav = new ModelAndView("businesses/businessDetails");
		Business business = businessRepository.findBySlugWithDetails(slug)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Business with slug '" + slug + "' not found."));
		mav.addObject(business);
		return mav;
	}

	/**
	 * Displays the edit form for an existing business, pre-populated with its current
	 * data. Only accessible to users with the {@code MANAGE_BUSINESSES} permission.
	 * @param businessId the numeric ID of the business to edit
	 * @param model the {@link Model} to populate with the existing {@link Business}
	 * @return the view name for the create/update business form
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if no business
	 * matches the given ID
	 */
	@GetMapping("/businesses/{businessId:\\d+}/edit")
	public String initUpdateForm(@PathVariable("businessId") int businessId, Model model) {
		Business business = businessRepository.findById(businessId)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Business with id " + businessId + " not found."));
		model.addAttribute("business", business);
		return "businesses/createOrUpdateBusinessForm";
	}

	/**
	 * Processes the edit form submission for an existing business. Validates input,
	 * preserves fields that should not be changed via the form, and persists the updated
	 * business.
	 * @param business the {@link Business} object populated from the submitted form
	 * @param result the {@link BindingResult} containing any validation errors
	 * @param businessId the numeric ID of the business being updated
	 * @return a redirect to the business slug URL on success, or the form view on
	 * validation failure
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if no business
	 * matches the given ID
	 */
	@PostMapping("/businesses/{businessId:\\d+}/edit")
	public String processUpdateForm(@Valid Business business, BindingResult result,
			@PathVariable("businessId") int businessId) {
		Business existing = businessRepository.findById(businessId)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Business with id " + businessId + " not found."));

		if (result.hasErrors()) {
			return "businesses/createOrUpdateBusinessForm";
		}

		business.setId(businessId);
		business.setIsVerified(existing.getIsVerified());
		business.setIsActive(existing.getIsActive());
		business.setSubmittedByUserId(existing.getSubmittedByUserId());

		businessRepository.save(business);

		String slug = Business.toSlug(business.getName());
		return "redirect:/businesses/" + slug;
	}

	/**
	 * Soft-deletes a business by its numeric ID. The {@code @SQLDelete} annotation on
	 * {@link Business} intercepts the delete and sets {@code deleted_at = NOW()}. Only
	 * accessible to users with the {@code MANAGE_BUSINESSES} permission.
	 * @param businessId the numeric ID of the business to delete
	 * @param redirectAttributes used to pass a flash success message to the list view
	 * @return a redirect to the business list
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if no business
	 * matches the given ID
	 */
	@PostMapping("/businesses/{businessId:\\d+}/delete")
	public String deleteBusiness(@PathVariable("businessId") int businessId, RedirectAttributes redirectAttributes) {
		Business business = businessRepository.findById(businessId)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Business with id " + businessId + " not found."));

		businessRepository.delete(business);
		redirectAttributes.addFlashAttribute("message", "Business \"" + business.getName() + "\" has been removed.");
		return "redirect:/businesses";
	}

	// -------------------------------------------------------------------------
	// Incentive CRUD (nested under /businesses/{slug}/incentives)
	// -------------------------------------------------------------------------

	/**
	 * Displays the form for adding a new incentive to a business. Only accessible to
	 * users with the {@code MANAGE_BUSINESSES} permission.
	 * @param slug the URL-friendly slug of the parent business
	 * @param model the {@link Model} to populate
	 * @return the view name for the incentive create/update form
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if no business
	 * matches the given slug
	 */
	@GetMapping("/businesses/{slug:[a-zA-Z][a-zA-Z0-9-]*}/incentives/new")
	public String initIncentiveCreationForm(@PathVariable("slug") String slug, Model model) {
		Business business = businessRepository.findBySlugWithDetails(slug)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Business with slug '" + slug + "' not found."));

		Incentive incentive = new Incentive();
		incentive.setBusiness(business);

		model.addAttribute("business", business);
		model.addAttribute("incentive", incentive);
		return "businesses/createOrUpdateIncentiveForm";
	}

	/**
	 * Processes the form submission for creating a new incentive. Validates the submitted
	 * data, associates the incentive with the parent business, then saves and redirects.
	 * @param slug the URL-friendly slug of the parent business
	 * @param incentive the {@link Incentive} object populated from the form
	 * @param result the {@link BindingResult} containing any validation errors
	 * @param selectedTypeIds the IDs of the selected {@link IncentiveType} checkboxes
	 * @param principal the currently authenticated user (used to record
	 * {@code submittedByUserId})
	 * @param model the {@link Model} to add attributes to on error
	 * @param redirectAttributes used to pass a flash success message on redirect
	 * @return a redirect to the business details page on success, or the form view on
	 * validation failure
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if no business
	 * matches the given slug
	 */
	@PostMapping("/businesses/{slug:[a-zA-Z][a-zA-Z0-9-]*}/incentives/new")
	public String processIncentiveCreationForm(@PathVariable("slug") String slug, @Valid Incentive incentive,
			BindingResult result, @RequestParam(value = "typeIds", required = false) List<Integer> selectedTypeIds,
			Principal principal, Model model, RedirectAttributes redirectAttributes) {

		Business business = businessRepository.findBySlugWithDetails(slug)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Business with slug '" + slug + "' not found."));

		if (result.hasErrors()) {
			model.addAttribute("business", business);
			return "businesses/createOrUpdateIncentiveForm";
		}

		incentive.setBusiness(business);
		resolveIncentiveTypes(incentive, selectedTypeIds);

		incentiveRepository.save(incentive);
		redirectAttributes.addFlashAttribute("message", "Incentive added successfully.");
		return "redirect:/businesses/" + slug;
	}

	/**
	 * Displays the edit form for an existing incentive, pre-populated with its current
	 * data. Only accessible to users with the {@code MANAGE_BUSINESSES} permission.
	 * @param slug the URL-friendly slug of the parent business
	 * @param incentiveId the numeric ID of the incentive to edit
	 * @param model the {@link Model} to populate
	 * @return the view name for the incentive create/update form
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if the business
	 * or incentive is not found
	 */
	@GetMapping("/businesses/{slug:[a-zA-Z][a-zA-Z0-9-]*}/incentives/{incentiveId:\\d+}/edit")
	public String initIncentiveUpdateForm(@PathVariable("slug") String slug,
			@PathVariable("incentiveId") int incentiveId, Model model) {

		Business business = businessRepository.findBySlugWithDetails(slug)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Business with slug '" + slug + "' not found."));

		Incentive incentive = incentiveRepository.findByIdWithTypes(incentiveId);
		if (incentive == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Incentive with id " + incentiveId + " not found.");
		}

		model.addAttribute("business", business);
		model.addAttribute("incentive", incentive);
		return "businesses/createOrUpdateIncentiveForm";
	}

	/**
	 * Processes the edit form submission for an existing incentive. Validates input,
	 * preserves the parent business association and audit fields, then saves.
	 * @param slug the URL-friendly slug of the parent business
	 * @param incentiveId the numeric ID of the incentive being updated
	 * @param incentive the {@link Incentive} object populated from the submitted form
	 * @param result the {@link BindingResult} containing any validation errors
	 * @param selectedTypeIds the IDs of the selected {@link IncentiveType} checkboxes
	 * @param model the {@link Model} to add attributes to on error
	 * @param redirectAttributes used to pass a flash success message on redirect
	 * @return a redirect to the business details page on success, or the form view on
	 * validation failure
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if the business
	 * or incentive is not found
	 */
	@PostMapping("/businesses/{slug:[a-zA-Z][a-zA-Z0-9-]*}/incentives/{incentiveId:\\d+}/edit")
	public String processIncentiveUpdateForm(@PathVariable("slug") String slug,
			@PathVariable("incentiveId") int incentiveId, @Valid Incentive incentive, BindingResult result,
			@RequestParam(value = "typeIds", required = false) List<Integer> selectedTypeIds, Model model,
			RedirectAttributes redirectAttributes) {

		Business business = businessRepository.findBySlugWithDetails(slug)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Business with slug '" + slug + "' not found."));

		Incentive existing = incentiveRepository.findByIdWithTypes(incentiveId);
		if (existing == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Incentive with id " + incentiveId + " not found.");
		}

		if (result.hasErrors()) {
			model.addAttribute("business", business);
			return "businesses/createOrUpdateIncentiveForm";
		}

		// Preserve immutable fields
		incentive.setId(incentiveId);
		incentive.setBusiness(business);
		incentive.setSubmittedByUserId(existing.getSubmittedByUserId());

		resolveIncentiveTypes(incentive, selectedTypeIds);

		incentiveRepository.save(incentive);
		redirectAttributes.addFlashAttribute("message", "Incentive updated successfully.");
		return "redirect:/businesses/" + slug;
	}

	/**
	 * Soft-deletes an incentive. The {@code @SQLDelete} annotation on {@link Incentive}
	 * intercepts the delete and sets {@code deleted_at = NOW()}. Only accessible to users
	 * with the {@code MANAGE_BUSINESSES} permission.
	 * @param slug the URL-friendly slug of the parent business
	 * @param incentiveId the numeric ID of the incentive to delete
	 * @param redirectAttributes used to pass a flash success message on redirect
	 * @return a redirect to the business details page
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if the incentive
	 * is not found
	 */
	@PostMapping("/businesses/{slug:[a-zA-Z][a-zA-Z0-9-]*}/incentives/{incentiveId:\\d+}/delete")
	public String deleteIncentive(@PathVariable("slug") String slug, @PathVariable("incentiveId") int incentiveId,
			RedirectAttributes redirectAttributes) {

		Incentive incentive = incentiveRepository.findById(incentiveId);
		if (incentive == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Incentive with id " + incentiveId + " not found.");
		}

		incentiveRepository.delete(incentive);
		redirectAttributes.addFlashAttribute("message", "Incentive \"" + incentive.getTitle() + "\" has been removed.");
		return "redirect:/businesses/" + slug;
	}

	/**
	 * Toggles the active/inactive status of an incentive without a full edit. Only
	 * accessible to users with the {@code MANAGE_BUSINESSES} permission.
	 * @param slug the URL-friendly slug of the parent business
	 * @param incentiveId the numeric ID of the incentive to toggle
	 * @param redirectAttributes used to pass a flash success message on redirect
	 * @return a redirect to the business details page
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if the incentive
	 * is not found
	 */
	@PostMapping("/businesses/{slug:[a-zA-Z][a-zA-Z0-9-]*}/incentives/{incentiveId:\\d+}/toggle")
	public String toggleIncentive(@PathVariable("slug") String slug, @PathVariable("incentiveId") int incentiveId,
			RedirectAttributes redirectAttributes) {

		Incentive incentive = incentiveRepository.findById(incentiveId);
		if (incentive == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Incentive with id " + incentiveId + " not found.");
		}

		boolean newStatus = !Boolean.TRUE.equals(incentive.getIsActive());
		incentive.setIsActive(newStatus);
		incentiveRepository.save(incentive);

		String statusLabel = newStatus ? "activated" : "deactivated";
		redirectAttributes.addFlashAttribute("message",
				"Incentive \"" + incentive.getTitle() + "\" has been " + statusLabel + ".");
		return "redirect:/businesses/" + slug;
	}

	// -------------------------------------------------------------------------
	// Legacy JSON endpoint (used by businessDetails.html JavaScript)
	// -------------------------------------------------------------------------

	/**
	 * Returns the active incentives for a business as a JSON array. Used by the business
	 * details page to dynamically display incentives.
	 * @param businessId the ID of the business
	 * @return list of incentives as JSON
	 */
	@GetMapping("businesses/{businessId}/incentives")
	@ResponseBody
	@org.springframework.transaction.annotation.Transactional(readOnly = true)
	public List<IncentiveDTO> getBusinessIncentives(@PathVariable Integer businessId) {
		List<Incentive> incentives = incentiveRepository.findByBusinessIdAndIsActive(businessId, true);
		return incentives.stream().map(IncentiveDTO::new).collect(java.util.stream.Collectors.toList());
	}

	// -------------------------------------------------------------------------
	// Private helpers
	// -------------------------------------------------------------------------

	/**
	 * Resolves and attaches {@link IncentiveType} instances to the given incentive based
	 * on a list of selected type IDs submitted from the form. Clears any previously
	 * attached types before applying the new selection.
	 * @param incentive the incentive to update
	 * @param selectedTypeIds the list of {@link IncentiveType} IDs to associate, or
	 * {@code null} if none were selected
	 */
	private void resolveIncentiveTypes(Incentive incentive, List<Integer> selectedTypeIds) {
		incentive.getIncentiveTypes().clear();
		if (selectedTypeIds != null) {
			for (Integer typeId : selectedTypeIds) {
				IncentiveType type = incentiveTypeRepository.findById(typeId);
				if (type != null) {
					incentive.getIncentiveTypes().add(type);
				}
			}
		}
	}

}
