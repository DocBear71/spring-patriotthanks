package org.springframework.samples.petclinic.codesignal;

import java.util.List;

public interface RecipeService {

	List<Recipe> getRecipesByType(String type);

	// New method for combined filtering
	List<Recipe> findByCategoryAndDietaryPreferenceIgnoreCase(String category, String dietaryPreference);

}
