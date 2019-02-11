package com.faizal.springrecipe.services;

import com.faizal.springrecipe.commands.IngredientCommand;

public interface IngredientService {

	IngredientCommand findByRecipeIdAndIngredientId(String recipeId, String id);

	IngredientCommand save(IngredientCommand ingredientCommand);

	void deleteById(String recipeId, String id);
}
