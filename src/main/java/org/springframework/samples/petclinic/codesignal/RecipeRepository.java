package org.springframework.samples.petclinic.codesignal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

	List<Recipe> findByCategory(String category);

	// Spring generates: SELECT * FROM recipes WHERE type = ?
	List<Recipe> findByTypeIgnoreCase(String type);

}
