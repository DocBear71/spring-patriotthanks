package org.springframework.samples.petclinic.school;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
public class SchoolController {

	private final SchoolRepository schoolRepository;

	public SchoolController(SchoolRepository schoolRepository) {

		this.schoolRepository = schoolRepository;
	}

	@GetMapping("schools")
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

	// Matches ONLY numbers (e.g., /schools/1)
	@GetMapping("/schools/{schoolId:\\d+}")
	public ModelAndView showSchoolById(@PathVariable("schoolId") int schoolId) {
		ModelAndView mav = new ModelAndView("schools/schoolDetails");
		School school = schoolRepository.findById(schoolId)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "School with id " + schoolId + " not found."));
		mav.addObject(school);
		return mav;
	}

	// Matches text (e.g., /schools/kirkwood)
	@GetMapping("/schools/{slug:[a-zA-Z-]+}")
	public ModelAndView showSchoolBySlug(@PathVariable("slug") String slug) {
		// Reconstruct the domain (User asked to assume ".edu")
		String fullDomain = slug + ".edu";

		ModelAndView mav = new ModelAndView("schools/schoolDetails");
		School school = schoolRepository.findByDomain(fullDomain)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "School with domain '" + fullDomain + "' not found."));
		mav.addObject(school);
		return mav;
	}

}
