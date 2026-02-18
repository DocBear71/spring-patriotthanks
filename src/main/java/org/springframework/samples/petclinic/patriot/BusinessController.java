package org.springframework.samples.petclinic.patriot;

import java.util.Collection;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for handling business-related web requests. Provides endpoints for viewing
 * paginated lists of businesses, their associated incentives, and creating new business
 * records.
 *
 * @author Edward McKeown
 */
@Controller
public class BusinessController {

	private final BusinessRepository businessRepository;

	private final IncentiveRepository incentiveRepository;

	private final BusinessTypeRepository businessTypeRepository;

	/**
	 * Constructor for BusinessController.
	 * @param businessRepository the repository for accessing business data
	 * @param incentiveRepository the repository for accessing incentive data
	 * @param businessTypeRepository the repository for accessing business type data
	 */
	public BusinessController(BusinessRepository businessRepository, IncentiveRepository incentiveRepository,
							  BusinessTypeRepository businessTypeRepository) {
		this.businessRepository = businessRepository;
		this.incentiveRepository = incentiveRepository;
		this.businessTypeRepository = businessTypeRepository;
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
	 * Displays a paginated list of businesses.
	 * @param page the page number to display (defaults to 1)
	 * @param model the Model to add attributes to
	 * @return the view name for the business list page
	 */
	@GetMapping("businesses")
	public String showBusinessList(@RequestParam(defaultValue = "1") int page, Model model) {
		// Pagination setup (10 items per page)
		Pageable pageable = PageRequest.of(page - 1, 10);
		Page<Business> businessPage = businessRepository.findAll(pageable);

		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", businessPage.getTotalPages());
		model.addAttribute("totalItems", businessPage.getTotalElements());
		model.addAttribute("listBusinesses", businessPage.getContent());

		return "businesses/businessList";
	}

	/**
	 * Displays the form for creating a new business. Phase 1 of the Add Business
	 * sequence: creates an empty Business object, adds it to the model, and returns the
	 * form view.
	 * @param model the Model to add attributes to
	 * @return the view name for the business creation form
	 */
	@GetMapping("/businesses/new")
	public String initCreationForm(Model model) {
		// Phase 1 of Sequence Diagram
		// 1. Create the blank object (State: New)
		Business business = new Business();
		// 2. Add it to the model so the Thymeleaf form can bind data to it
		model.addAttribute("business", business);
		// 3. Return the view
		return "businesses/createOrUpdateBusinessForm";
	}

	/**
	 * Processes the form submission for creating a new business. Phase 2 of the Add
	 * Business sequence: validates the submitted data and either saves the business and
	 * redirects to the list, or returns the form with error messages.
	 * @param business the Business object populated from the form data
	 * @param result the BindingResult containing any validation errors
	 * @param model the Model to add attributes to
	 * @return a redirect to the business list on success, or the form view on validation
	 * failure
	 */
	@PostMapping("/businesses/new")
	public String processCreationForm(@Valid Business business, BindingResult result, Model model) {
		// Phase 2 of Sequence Diagram
		// 1. Check Validation
		if (result.hasErrors()) {
			// Validation Failed: Return to the form to show errors
			return "businesses/createOrUpdateBusinessForm";
		}
		// 2. Save Data (Validation Passed)
		// Note: isActive defaults to true and isVerified defaults to false
		// because of the Business.java definition
		businessRepository.save(business);
		// 3. Redirect to the list
		return "redirect:/businesses";
	}

	/**
	 * AJAX endpoint to retrieve incentives for a specific business. Returns JSON data
	 * for dynamic loading via JavaScript.
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

}
