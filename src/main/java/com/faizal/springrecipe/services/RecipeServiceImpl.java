package com.faizal.springrecipe.services;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.faizal.springrecipe.commands.RecipeCommand;
import com.faizal.springrecipe.converters.RecipeCommandToRecipe;
import com.faizal.springrecipe.converters.RecipeToRecipeCommand;
import com.faizal.springrecipe.domain.Recipe;
import com.faizal.springrecipe.exceptions.NotFoundException;
import com.faizal.springrecipe.repositories.RecipeRepository;

@Service
public class RecipeServiceImpl implements RecipeService {

	private final RecipeRepository recipeRepository;
	private final RecipeCommandToRecipe recipeCommandToRecipe;
	private final RecipeToRecipeCommand recipeToRecipeCommand;

	public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeCommandToRecipe recipeCommandToRecipe,
			RecipeToRecipeCommand recipeToRecipeCommand) {
		this.recipeRepository = recipeRepository;
		this.recipeCommandToRecipe = recipeCommandToRecipe;
		this.recipeToRecipeCommand = recipeToRecipeCommand;
	}

	@Override
	public Set<Recipe> findAll() {
		Set<Recipe> recipeSet = new HashSet<>();
		recipeRepository.findAll().iterator().forEachRemaining(recipeSet::add);
		return recipeSet;
	}

	@Override
	public Recipe findById(String id) {
		Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
		if (optionalRecipe.isPresent())
			return optionalRecipe.get();
		else
			throw new NotFoundException("Recipe Not Found. For id value :" + id);
	}

	@Override
	@Transactional
	public RecipeCommand save(RecipeCommand command) {
		return recipeToRecipeCommand.convert(recipeRepository.save(recipeCommandToRecipe.convert(command)));
	}

	@Override
	public RecipeCommand findCommandById(String id) {
		return recipeToRecipeCommand.convert(this.findById(id));
	}

	@Override
	public void deleteById(String id) {
		recipeRepository.deleteById(id);
	}

}