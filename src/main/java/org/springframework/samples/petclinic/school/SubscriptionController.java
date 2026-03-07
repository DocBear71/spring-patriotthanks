package org.springframework.samples.petclinic.school;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collection;
import java.util.Map;

@Controller
public class SubscriptionController {

	private final SubscriptionRepository subscriptionRepository;

	public SubscriptionController(SubscriptionRepository subscriptionRepository) {
		this.subscriptionRepository = subscriptionRepository;
	}

	@GetMapping("/pricing")
	public String showPricingTable(Model model) {
		Collection<Subscription> subscriptions = subscriptionRepository.findAllBy();
		model.addAttribute("subscriptions", subscriptions.stream().toList());
		return "pricing/pricing";
	}

	@GetMapping("/subscription/new")
	public String initCreationForm(Map<String, Object> model) {
		Subscription subscription = new Subscription();
		model.put("subscription", subscription);
		return "schools/createOrUpdateSubscriptionForm";
	}

	@PostMapping("/subscription/new")
	public String processCreationForm(@Valid Subscription subscription, BindingResult result) {
		if (result.hasErrors()) {
			return "schools/createOrUpdateSubscriptionForm";
		}
		subscriptionRepository.save(subscription);
		return "redirect:/pricing";
	}

}
