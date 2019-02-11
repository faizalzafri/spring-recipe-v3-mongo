package com.faizal.springrecipe.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.faizal.springrecipe.commands.IngredientCommand;
import com.faizal.springrecipe.converters.IngredientCommandToIngredient;
import com.faizal.springrecipe.converters.IngredientToIngredientCommand;
import com.faizal.springrecipe.domain.Ingredient;
import com.faizal.springrecipe.domain.Recipe;
import com.faizal.springrecipe.repositories.RecipeRepository;
import com.faizal.springrecipe.repositories.UnitOfMeasureRepository;

@Service
public class IngredientServiceImpl implements IngredientService {

	private final RecipeRepository recipeRepository;
	private IngredientToIngredientCommand irCommand;
	private final UnitOfMeasureRepository unitOfMeasureRepository;
	IngredientCommandToIngredient ingredientCommandToIngredient;

	public IngredientServiceImpl(RecipeRepository recipeRepository, IngredientToIngredientCommand irCommand,
			UnitOfMeasureRepository unitOfMeasureRepository,
			IngredientCommandToIngredient ingredientCommandToIngredient) {
		this.recipeRepository = recipeRepository;
		this.irCommand = irCommand;
		this.unitOfMeasureRepository = unitOfMeasureRepository;
		this.ingredientCommandToIngredient = ingredientCommandToIngredient;
	}

	@Override
	public IngredientCommand findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {

		Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

		if (!recipeOptional.isPresent()) {
			throw new RuntimeException("recipe id not found. Id: " + recipeId);
		}

		Recipe recipe = recipeOptional.get();

		Optional<IngredientCommand> ingredientCommandOptional = recipe.getIngredients().stream()
				.filter(ing -> ing.getId().equals(ingredientId)).map(irCommand::convert).findFirst();

		if (!ingredientCommandOptional.isPresent()) {
			throw new RuntimeException("Ingredient id not found: " + ingredientId);
		}

		return ingredientCommandOptional.get();
	}

	@Override
	@Transactional
	public IngredientCommand save(IngredientCommand ingredientCommand) {

		Optional<Recipe> recipeOptional = recipeRepository.findById(ingredientCommand.getRecipeId());

		if (!recipeOptional.isPresent()) {
			return new IngredientCommand();
		} else {
			Recipe recipe = recipeOptional.get();

			Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
					.filter(ing -> ing.getId().equals(ingredientCommand.getId())).findFirst();

			if (ingredientOptional.isPresent()) {
				Ingredient foundIngredient = ingredientOptional.get();
				foundIngredient.setDescription(ingredientCommand.getDescription());
				foundIngredient.setAmount(ingredientCommand.getAmount());
				foundIngredient.setUom(unitOfMeasureRepository.findById(ingredientCommand.getUom().getId())
						.orElseThrow(() -> new RuntimeException("No UOM")));

			} else {
				Ingredient ingredient = ingredientCommandToIngredient.convert(ingredientCommand);
				ingredient.setRecipe(recipe);
				recipe.addIngredient(ingredient);

			}

			Recipe savedRecipe = recipeRepository.save(recipe);

			Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
					.filter(ing -> ing.getId().equals(ingredientCommand.getId())).findFirst();

			if (!savedIngredientOptional.isPresent()) {
				savedIngredientOptional = savedRecipe.getIngredients().stream()
						.filter(ing -> ing.getDescription().equals(ingredientCommand.getDescription()))
						.filter(ing -> ing.getAmount().equals(ingredientCommand.getAmount()))
						.filter(ing -> ing.getUom().getId().equals(ingredientCommand.getUom().getId())).findFirst();
			}

			return irCommand.convert(savedIngredientOptional.get());
		}
	}

	@Override
	public void deleteById(String recipeId, String id) {
		Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);

		if (optionalRecipe.isPresent()) {
			Recipe recipe = optionalRecipe.get();

			Optional<Ingredient> optionalIngredient = recipe.getIngredients().stream()
					.filter(ing -> ing.getId().equals(id)).findFirst();

			if (optionalIngredient.isPresent()) {
				Ingredient ingredient = optionalIngredient.get();
				ingredient.setRecipe(null);
				recipe.getIngredients().remove(ingredient);
				recipeRepository.save(recipe);
			} else {
				throw new RuntimeException("Recipe id not found");
			}
		}
	}

}